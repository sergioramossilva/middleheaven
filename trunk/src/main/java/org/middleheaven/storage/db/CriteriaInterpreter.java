package org.middleheaven.storage.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.middleheaven.storage.criteria.FieldInSetCriteria;
import org.middleheaven.storage.criteria.FieldValueHolder;
import org.middleheaven.storage.criteria.JuntionCriterion;
import org.middleheaven.storage.criteria.LogicCriterion;
import org.middleheaven.storage.criteria.MaxFieldOperator;
import org.middleheaven.storage.criteria.MinFieldOperator;
import org.middleheaven.storage.criteria.OrderingCriterion;
import org.middleheaven.storage.criteria.Projection;
import org.middleheaven.storage.criteria.ProjectionOperator;
import org.middleheaven.storage.criteria.SingleObjectValueHolder;
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

	public RetriveDataBaseCommand translateRetrive(){
		
		StringBuilder sqlBuilder = new StringBuilder("SELECT ");
		List<FieldValueHolder> params = new LinkedList<FieldValueHolder>();
		
		// LIMITS: distinct and  top n 
		writeStartLimitClause(sqlBuilder);

		// RETURN CLAUSE
		writeResultColumnsClause(sqlBuilder);

		// FROM CLAUSE 
		writeFromClause(sqlBuilder);

		// WHERE CLAUSE
		writeWhereClause(sqlBuilder,params,true);

		// GROUP BY
		writeGroupByClause(sqlBuilder);

		// ORDER BY
		writeOrderByClause(sqlBuilder);


		// LIMITS: Limit and offset
		writeEndLimitClause(sqlBuilder);

		return new SQLRetriveCommand(sqlBuilder.toString(),params);
	}
	


	public DataBaseCommand translateDelete(){
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ")
		.append(model.hardNameForEntity());

		List<FieldValueHolder> params = new LinkedList<FieldValueHolder>();
		
		// WHERE CLAUSE
		writeWhereClause(sqlBuilder,params,false);

		return new SQLDeleteCommand(sqlBuilder.toString(),params);
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
				dialect().writeQueryHardname(queryBuffer, model().fieldModel(name).getHardName());

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
			if (this.criteria().resultFields().isEmpty()){
				queryBuffer.append(" * ");
			} else {
				for (QualifiedName name : this.criteria().resultFields() ){

					StorableFieldModel fm = model().fieldModel(name);
					if (fm.isTransient()){ // if field is not persistable
						continue;
					}

					if (!first){
						queryBuffer.append(' ').append(',').append(' ');
					}
					first = false;

					dialect().writeQueryHardname(queryBuffer , fm.getHardName());

				}
			}
		}
	}



	protected void writeEndLimitClause(StringBuilder selectBuffer){
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

				dialect().writeQueryHardname(queryBuffer, model.fieldModel(criterion.getFieldName()).getHardName());


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

				dialect().writeQueryHardname(queryBuffer,  model().fieldModel(name).getHardName());

				if (it.hasNext()){
					queryBuffer.append(" , ");
				}
			}


		}
	}

	protected void writeWhereClause(StringBuilder queryBuffer, List<FieldValueHolder> params, boolean isQuery ){
		// WHERE CLAUSE
		// Cria primeiro a sentença. 
		// Se não houver nenhum o where não é adicionado
		if ( criteria().restrictions().criteriaCount()==0){
			return;
		}

		StringBuilder whereClause =  new StringBuilder();
		StringBuilder joinClause =  new StringBuilder();
		translateCriteriaToWhereClause(whereClause,params, joinClause, criteria().restrictions().simplify());;

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
		Criteria<?> emptyCriteria =  CriteriaBuilder.search(criteria.getTargetClass()).limit(0).all();

		emptyCriteria.setKeyOnly(true);

		CriteriaInterpreter ci = dialect().newCriteriaInterpreter(criteria(),model());

		StringBuilder queryBuffer = new StringBuilder();

		criteriaBuffer.append(' ');
		dialect().writeQueryHardname(criteriaBuffer, model.fieldModel(criterion.getFieldName()).getHardName());

		if (!criterion.isIncluded()){
			queryBuffer.append(" NOT ");
		}

		queryBuffer.append(" IN (");
		queryBuffer.append(ci.translateRetrive().toString());
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
		dialect().writeQueryHardname(criteriaBuffer, model.fieldModel(criterion.getFieldName()).getHardName());


		if (!criterion.isIncluded()){
			queryBuffer.append(" NOT ");
		}
		
		RetriveDataBaseCommand r = ci.translateRetrive();
		
		queryBuffer.append(" IN (")
		.append(r.toString()) // TODO merge param values 
		.append(")");

		criteriaBuffer.append(queryBuffer);
	}


	protected void translateCriteriaToWhereClause(StringBuilder criteriaBuffer, List<FieldValueHolder> params, StringBuilder joinClause ,Criterion criterion){
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

				// do not write WHERE claus because it will been writen in the normal process
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
					dialect().writeQueryHardname(criteriaBuffer, model().fieldModel(f.getFieldName()).getHardName());

					if (!f.isIncluded()){
						criteriaBuffer.append(" NOT ");
					}
					criteriaBuffer.append(" IN (");
					StorableFieldModel fm = model().fieldModel(f.getFieldName());
					
					Collection<Object> values = (Collection<Object>) f.valueHolder().getValue();
					for (Iterator<Object> it = values.iterator(); it.hasNext();){
						params.add(new SingleObjectValueHolder(it.next(), fm.getDataType()));
						criteriaBuffer.append("?,");
					}

					criteriaBuffer.delete(criteriaBuffer.length()-1, criteriaBuffer.length());
					criteriaBuffer.append(")");
				}
			}
		}else if (criterion instanceof FieldCriterion){
			FieldCriterion f = (FieldCriterion)criterion;

			StorableFieldModel fm = model().fieldModel(f.getFieldName());
			if (fm==null){
				throw new IllegalStateException(f.getFieldName() + " not found");
			}
			FieldValueHolder vholder = f.valueHolder();

			if (!fm.isTransient() && !fm.isVersion()){
				if (vholder.isEmpty()){
					// empty value implies compare to NULL
					criteriaBuffer.append(' ');
					dialect().writeQueryHardname(criteriaBuffer, fm.getHardName());

					if (CriterionOperator.EQUAL.equals(f.getOperator())){
						if (f.getOperator().isNegated()){
							criteriaBuffer.append(" IS NOT NULL ");
						} else {
							criteriaBuffer.append(" IS NULL ");
						}
					} else {
						criteriaBuffer.append(" IS NOT NULL ");
					}

					criteriaBuffer.append(' ');
				} else if (!fm.getDataType().isVirtual()){

					criteriaBuffer.append(' ');
					dialect().writeQueryHardname(criteriaBuffer, fm.getHardName());

					if (f.getOperator().isNegated()){
						if (CriterionOperator.CONTAINS.equals(f.getOperator()) || 
								CriterionOperator.STARTS_WITH.equals(f.getOperator()) || 
								CriterionOperator.ENDS_WITH.equals(f.getOperator())
							){
							criteriaBuffer.append(" NOT LIKE ? ");
						} else if (CriterionOperator.GREATER_THAN.equals(f.getOperator())){
							criteriaBuffer.append(" <= ? ");
						} else if (CriterionOperator.GREATER_THAN_OR_EQUAL.equals(f.getOperator())){
							criteriaBuffer.append(" < ? ");
						}  else if (CriterionOperator.LESS_THAN.equals(f.getOperator())){
							criteriaBuffer.append(" >= ? ");
						}  else if (CriterionOperator.LESS_THAN_OR_EQUAL.equals(f.getOperator())){
							criteriaBuffer.append(" > ? ");
						} else if (CriterionOperator.EQUAL.equals(f.getOperator())){
							criteriaBuffer.append(" <> ? ");
						} else if (CriterionOperator.UNKOWN.equals(f.getOperator())){
							//logWarn("Criterion used an unkown match operator. Using equal instead");
							criteriaBuffer.append(" = ? ");
						} 
					} else { // positive operators
						if (CriterionOperator.CONTAINS.equals(f.getOperator()) || 
								CriterionOperator.STARTS_WITH.equals(f.getOperator()) || 
								CriterionOperator.ENDS_WITH.equals(f.getOperator())
							){
							criteriaBuffer.append(" LIKE ? ");
						} else if (CriterionOperator.GREATER_THAN.equals(f.getOperator())){
							criteriaBuffer.append(" > ? ");
						} else if (CriterionOperator.GREATER_THAN_OR_EQUAL.equals(f.getOperator())){
							criteriaBuffer.append(" >= ? ");
						}  else if (CriterionOperator.LESS_THAN.equals(f.getOperator())){
							criteriaBuffer.append(" < ? ");
						}  else if (CriterionOperator.LESS_THAN_OR_EQUAL.equals(f.getOperator())){
							criteriaBuffer.append(" <= ? ");
						} else if (CriterionOperator.UNKOWN.equals(f.getOperator())){
							//logWarn("Criterion used an unkown match operator. Using equal instead");
							criteriaBuffer.append(" = ? ");
						} else if (CriterionOperator.EQUAL.equals(f.getOperator())){
							criteriaBuffer.append(" = ? ");
						}
					}
					
				

					params.add(vholder);
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
						translateCriteriaToWhereClause(criteriaBuffer,params,joinClause,c);
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
						translateCriteriaToWhereClause(criteriaBuffer,params,joinClause,c);
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
			dialect().writeQueryHardname(selectBuffer, this.model().fieldModel(n).getHardName());
			selectBuffer.append(") AS ");
			selectBuffer.append(n.getColumnName().toLowerCase());
		} else if (op instanceof MaxFieldOperator){
			selectBuffer.append("MAX(");
			QualifiedName n = ((MaxFieldOperator)op).getFieldName();
			dialect().writeQueryHardname(selectBuffer, this.model().fieldModel(n).getHardName());
			selectBuffer.append(") AS ");
			selectBuffer.append(n.getColumnName().toLowerCase());
		} else if (op instanceof MinFieldOperator){
			selectBuffer.append("MIN(");
			QualifiedName n = ((MinFieldOperator)op).getFieldName();
			dialect().writeQueryHardname(selectBuffer, this.model().fieldModel(n).getHardName());
			selectBuffer.append(") AS ");
			selectBuffer.append(n.getColumnName().toLowerCase());
		} else {
			throw new StorageException("ProjectionOperator " + op.getClass().getName() + " is not supported by " + this.getClass().getName());
		}
	}

}
