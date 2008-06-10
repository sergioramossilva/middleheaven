package org.middleheaven.storage.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.middleheaven.classification.LogicOperator;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.CountOperator;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.criteria.Criterion;
import org.middleheaven.storage.criteria.CriterionOperator;
import org.middleheaven.storage.criteria.EmptyCriterion;
import org.middleheaven.storage.criteria.FieldCriterion;
import org.middleheaven.storage.criteria.FieldValueHolder;
import org.middleheaven.storage.criteria.JuntionCriterion;
import org.middleheaven.storage.criteria.LogicCriterion;
import org.middleheaven.storage.criteria.MaxFieldOperator;
import org.middleheaven.storage.criteria.MinFieldOperator;
import org.middleheaven.storage.criteria.OrderingCriterion;
import org.middleheaven.storage.criteria.Projection;
import org.middleheaven.storage.criteria.ProjectionOperator;
import org.middleheaven.storage.criteria.SumFieldOperator;


public class CriteriaInterpreter {

	private DataBaseDialect dataBaseDialect;
	private Criteria<?> criteria;
	private StorableEntityModel model;

	public CriteriaInterpreter(DataBaseDialect dataBaseDialect,Criteria<?> criteria, StorableEntityModel model) {
		this.dataBaseDialect = dataBaseDialect;
		this.criteria = criteria;
		this.model = model;
	}

	protected DataBaseDialect dialect(){
		return this.dataBaseDialect;
	}

	protected Criteria<?> criteria(){
		return this.criteria;
	}

	protected StorableEntityModel model(){
		return this.model;
	}

	public String translate(){
		StringBuilder sqlBuilder = new StringBuilder("SELECT ");

		// LIMITS: distinct and  top n 
		writeStartLimitClause(sqlBuilder);

		// RETURN CLAUSE
		writeResultColumnsClause(sqlBuilder);

		// FROM CLAUSE 
		writeFromClause(sqlBuilder);

		// WHERE CLAUSE
		writeWhereClause(sqlBuilder);

		// GROUP BY
		writeGroupByClause(sqlBuilder);

		// ORDER BY
		writeOrderByClause(sqlBuilder);


		// LIMITS: Limit and offset
		writeEndLimitClause(sqlBuilder);

		return sqlBuilder.toString();
	}

	protected void writeStartLimitClause(StringBuilder selectBuffer) {
		if (criteria.isDistinct()){
			selectBuffer.append(" DISTINCT ");
		} 
	}



	protected void writeResultColumnsClause (StringBuilder queryBuffer){
		
		// if this is a projection criteria
		if (this.criteria().projection() !=null){

			// add groupBy
			Projection aggregation = this.criteria().projection();
			for (Iterator<QualifiedName> it = aggregation.groups().iterator();it.hasNext();){
				QualifiedName name = it.next();
				
				// get hardname
				dialect().writeHardname(queryBuffer, model().fieldModel(name).getHardName());

				if (it.hasNext()){
					queryBuffer.append(' ').append(',').append(' ');
				}
			}

			if (!aggregation.groups().isEmpty() && !aggregation.functions().isEmpty()){
				queryBuffer.append(' ').append(',').append(' ');
			}

			// add operators
			for (Iterator<ProjectionOperator> it = aggregation.functions().iterator();it.hasNext();){
				translateAggregationOperator(it.next() , queryBuffer);

				if (it.hasNext()){
					queryBuffer.append(' ').append(',').append(' ');
				}
			}

		}else {
			// this a normal reading criteria

			// run thought the fields. The transient cannot be read so do not use them
			boolean first = true;
			for (QualifiedName name : this.criteria().resultFields() ){
				
				StorableFieldModel fm = model().fieldModel(name);
				if (fm.isTransient()){ // if field is not persistable
					continue;
				}

				if (!first){
					queryBuffer.append(' ').append(',').append(' ');
				}
				first = false;

				dialect().writeHardname(queryBuffer , fm.getHardName());

			}
		}
	}



	protected void writeEndLimitClause(Appendable selectBuffer){
		// no-op
	}

	protected void writeFromClause(StringBuilder queryBuffer){

		// FROM ClAUSE
		queryBuffer.append(" FROM ");
		queryBuffer.append(dialect().startDelimiter());
		queryBuffer.append(model.hardNameForEntity().toLowerCase());
		queryBuffer.append(dialect().endDelimiter());

	}

	protected void writeOrderByClause(StringBuilder queryBuffer){

		// do not write order by order by if its a count query
		if (queryBuffer.indexOf("COUNT(")>=0){
			return;
		}

		// ORDER BY
		if (!criteria().ordering().isEmpty()){
			queryBuffer.append(" ORDER BY ");
			boolean first = true;
			for (OrderingCriterion criterion  : criteria().ordering()){

				if (!first){
					queryBuffer.append(", ");
				}
				first=false;

				dialect().writeHardname(queryBuffer, model.fieldModel(criterion.getFieldName()).getHardName());


				queryBuffer.append(criterion.isDescendant()?" desc":" asc");

			}
		}
	}

	protected void writeGroupByClause(StringBuilder queryBuffer){
		//GROUP BY
		Projection aggregation = criteria.projection();
		if (aggregation !=null && !aggregation.groups().isEmpty()){
			queryBuffer.append(" GROUP BY ");

			// write the name of all groups
			for (Iterator<QualifiedName> it = aggregation.groups().iterator(); it.hasNext();){
				QualifiedName name = it.next();

				dialect().writeHardname(queryBuffer,  model().fieldModel(name).getHardName());

				if (it.hasNext()){
					queryBuffer.append(" , ");
				}
			}


		}
	}

	protected void writeWhereClause(StringBuilder queryBuffer ){
		// WHERE CLAUSE
		// Cria primeiro a sentença. 
		// Se não houver nenhum o where não é adicionado
		if ( criteria().restrictions().criteriaCount()==0){
			return;
		}

		StringBuilder whereClause =  new StringBuilder();
		StringBuilder joinClause =  new StringBuilder();
		translateCriteriaToWhereClause(whereClause,joinClause, criteria().restrictions().simplify());;

		queryBuffer.append(joinClause);

		if (whereClause.length()>2){
			queryBuffer.append(" WHERE ");
			queryBuffer.append(whereClause);
		}

	}



	protected void translateFieldInQueryCriteriaToEmptySetInClause(StringBuilder criteriaBuffer,FieldInSetCriteria criterion){
		// [NOT] IN ( foo ) 
		// where foo is return nothing select 
		// made using range(0)
		Criteria<?> emptyCriteria =  CriteriaBuilder.createCriteria(criteria.getTargetClass()).setRange(0);

		emptyCriteria.setKeyOnly(true);

		CriteriaInterpreter ci = dialect().newCriteriaInterpreter(criteria(),model());

		StringBuilder queryBuffer = new StringBuilder();

		criteriaBuffer.append(' ');
		dialect().writeHardname(criteriaBuffer, model.fieldModel(criterion.getFieldName()).getHardName());

		if (!criterion.isIncluded()){
			queryBuffer.append(" NOT ");
		}

		queryBuffer.append(" IN (");
		queryBuffer.append(ci.translate());
		queryBuffer.append(")");

		criteriaBuffer.append(queryBuffer);
	}

	protected void translateFieldInQueryCriteriaToWhereClause(StringBuilder criteriaBuffer,FieldInSetCriteria criterion){
	
		// [NOT] IN ( Select field FROM table ... ) 
		Criteria<?> c = (Criteria<?>)criterion.valueHolder().getValue();
		c.setDistinct(true);
		
		CriteriaInterpreter ci = dialect().newCriteriaInterpreter(c, model);
		
		StringBuilder queryBuffer = new StringBuilder();

		criteriaBuffer.append(' ');
		dialect().writeHardname(criteriaBuffer, model.fieldModel(criterion.getFieldName()).getHardName());


		if (!criterion.isIncluded()){
			queryBuffer.append(" NOT ");
		}
		queryBuffer.append(" IN (")
		.append(ci.translate())
		.append(")");

		criteriaBuffer.append(queryBuffer);
	}


	protected void translateCriteriaToWhereClause(StringBuilder criteriaBuffer, StringBuilder joinClause ,Criterion criterion){
		if (criterion instanceof EmptyCriterion){
			criteriaBuffer.append("TRUE");
		} else if (criterion instanceof JuntionCriterion ) {

			JuntionCriterion jr = (JuntionCriterion)criterion;
			StorableFieldModel fm = model.fieldModel(jr.getFieldName());

			if (fm.getDataType().isToOneReference()){
				// join
				QualifiedName kfm = model().keyFieldModel().getHardName();

				joinClause.append(" INNER JOIN ")
				.append(dialect().startDelimiter())
				.append(model.hardNameForEntity())
				.append(dialect().endDelimiter())
				.append(" ON ")
				.append(dialect().startDelimiter())
				.append(fm.getHardName().getTableName())
				.append(dialect().endDelimiter())
				.append(dialect().fieldSeparator())
				.append(dialect().startDelimiter())
				.append(fm.getHardName().getColumnName())
				.append(dialect().endDelimiter())
				.append(" = ")
				.append(dialect().startDelimiter())
				.append(kfm.getTableName())
				.append(dialect().endDelimiter())
				.append(dialect().fieldSeparator())
				.append(dialect().startDelimiter())
				.append(kfm.getColumnName())
				.append(dialect().endDelimiter());

				// não escreve no where porque isso será feito pelo processo normal
				// já que , mesmos os campos de outras entidades estão no restrictions da base
			}


		}else if (criterion instanceof FieldInSetCriteria){
			FieldInSetCriteria f = (FieldInSetCriteria)criterion;

			if (f.useCriteria()){
				// values are obtain in a sub query
				translateFieldInQueryCriteriaToWhereClause(criteriaBuffer , f );
			} else {
				// values are obtain explicitly. use IN () command

				if (f.valueHolder().isEmpty()){

					translateFieldInQueryCriteriaToEmptySetInClause(criteriaBuffer , f );

				} else {
					dialect().writeHardname(criteriaBuffer, model().fieldModel(f.getFieldName()).getHardName());

					if (!f.isIncluded()){
						criteriaBuffer.append(" NOT ");
					}
					criteriaBuffer.append(" IN (");
					Collection<Object> values = (Collection<Object>) f.valueHolder().getValue();
					for (Iterator<Object> it = values.iterator(); it.hasNext();){
						//fields.add(new DefaultFieldValueHolder((Value)it.next() , ComparisonOperator.IN));
						criteriaBuffer.append(" ?");
						if (it.hasNext()){
							criteriaBuffer.append(", ");
						}
					}
					criteriaBuffer.append(")");
				}
			}
		}else if (criterion instanceof FieldCriterion){
			FieldCriterion f = (FieldCriterion)criterion;

			StorableFieldModel fm = model().fieldModel(f.getFieldName());
			FieldValueHolder vholder = f.valueHolder();

			if (!fm.isTransient() && !fm.isVersion()){
				if (vholder.isEmpty()){
					// valor vazio significa comparar com null
					criteriaBuffer.append(' ');
					dialect().writeHardname(criteriaBuffer, fm.getHardName());

					if (CriterionOperator.EQUAL.equals(f.getOperator())){
						criteriaBuffer.append(" IS NULL ");
					} else if (CriterionOperator.NOT_EQUAL.equals(f.getOperator())){
						criteriaBuffer.append(" IS NOT NULL ");
					} else {
						// logWarn("Operator " + f.getOperator() + " is not supported for empty value. Using not null instead");
						criteriaBuffer.append(" IS NOT NULL ");
					}

					criteriaBuffer.append(' ');
				} else if (!fm.getDataType().isVirtual()){

					criteriaBuffer.append(' ');
					dialect().writeHardname(criteriaBuffer, fm.getHardName());

					if (CriterionOperator.CONTAINS.equals(f.getOperator())){
						criteriaBuffer.append(" LIKE ? ");
					} else if (CriterionOperator.GREATER_THAN.equals(f.getOperator())){
						criteriaBuffer.append(" > ? ");
					} else if (CriterionOperator.GREATER_THAN_OR_EQUAL.equals(f.getOperator())){
						criteriaBuffer.append(" >= ? ");
					}  else if (CriterionOperator.LESS_THAN.equals(f.getOperator())){
						criteriaBuffer.append(" < ? ");
					}  else if (CriterionOperator.LESS_THAN_OR_EQUAL.equals(f.getOperator())){
						criteriaBuffer.append(" <= ? ");
					} else if (CriterionOperator.NOT_EQUAL.equals(f.getOperator())){
						criteriaBuffer.append(" <> ? ");
					} else if (CriterionOperator.UNKOWN.equals(f.getOperator())){
						//logWarn("Criterion used an unkown match operator. Using equal instead");
						criteriaBuffer.append(" = ? ");
					} else if (CriterionOperator.EQUAL.equals(f.getOperator())){
						criteriaBuffer.append(" = ? ");
					}

					//fields.add(f);
				}
			}
		}else if (criterion instanceof LogicCriterion){
			Collection<Criterion> criterias = ((LogicCriterion)criterion).criterias();
			if (!criterias.isEmpty()){

				// primeiro processa os criterios do tipo JuntionRestriction, se algum
				List<Criterion> others = new ArrayList<Criterion>(criterias.size());
				for (Iterator<Criterion> it = criterias.iterator(); it.hasNext();){
					Criterion c = (Criterion)it.next();
					if (c instanceof JuntionCriterion){
						translateCriteriaToWhereClause(criteriaBuffer,joinClause,c);
					} else {
						others.add(c);
					}
				}

				if (!others.isEmpty()){
					// depois processa os criterios de outros tipos
					LogicOperator opOperator = ((LogicCriterion)criterion).getOperator();
					final String op = opOperator.toString();
					criteriaBuffer.append('(');
					boolean writeOperator = false;
					for (Criterion c : others){
						if (writeOperator){
							criteriaBuffer.append(op);
							criteriaBuffer.append(' ');
						}
						int len = criteriaBuffer.length();
						translateCriteriaToWhereClause(criteriaBuffer,joinClause,c);
						writeOperator = (criteriaBuffer.length()>len);
					}
					criteriaBuffer.append(')');
				}
			}
		} 
	}


	protected  void translateAggregationOperator (ProjectionOperator op , StringBuilder selectBuffer ){
		if (op instanceof CountOperator){
			QualifiedName name = ((SumFieldOperator)op).getFieldName();
			if (name==null){
				selectBuffer.append("COUNT(*) AS count");
			} else {
				selectBuffer.append("COUNT(").append(this.model().fieldModel(name).getHardName()).append(") AS count");
			}
		
		} else if (op instanceof SumFieldOperator){
			selectBuffer.append("SUM(");
			QualifiedName n = ((SumFieldOperator)op).getFieldName();
			dialect().writeHardname(selectBuffer, this.model().fieldModel(n).getHardName());
			selectBuffer.append(") AS ");
			selectBuffer.append(n.getColumnName().toLowerCase());
		} else if (op instanceof MaxFieldOperator){
			selectBuffer.append("MAX(");
			QualifiedName n = ((MaxFieldOperator)op).getFieldName();
			dialect().writeHardname(selectBuffer, this.model().fieldModel(n).getHardName());
			selectBuffer.append(") AS ");
			selectBuffer.append(n.getColumnName().toLowerCase());
		} else if (op instanceof MinFieldOperator){
			selectBuffer.append("MIN(");
			QualifiedName n = ((MinFieldOperator)op).getFieldName();
			dialect().writeHardname(selectBuffer, this.model().fieldModel(n).getHardName());
			selectBuffer.append(") AS ");
			selectBuffer.append(n.getColumnName().toLowerCase());
		} else {
			throw new StorageException("ProjectionOperator " + op.getClass().getName() + " is not supported by " + this.getClass().getName());
		}
	}

}
