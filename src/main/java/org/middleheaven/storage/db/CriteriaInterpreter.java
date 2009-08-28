package org.middleheaven.storage.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.CountOperator;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.criteria.Criterion;
import org.middleheaven.storage.criteria.CriterionOperator;
import org.middleheaven.storage.criteria.EmptyCriterion;
import org.middleheaven.storage.criteria.FieldCriterion;
import org.middleheaven.storage.criteria.FieldInSetCriterion;
import org.middleheaven.storage.criteria.FieldValueHolder;
import org.middleheaven.storage.criteria.JunctionCriterion;
import org.middleheaven.storage.criteria.LogicCriterion;
import org.middleheaven.storage.criteria.MaxFieldOperator;
import org.middleheaven.storage.criteria.MinFieldOperator;
import org.middleheaven.storage.criteria.OrderingCriterion;
import org.middleheaven.storage.criteria.Projection;
import org.middleheaven.storage.criteria.ProjectionOperator;
import org.middleheaven.storage.criteria.SumFieldOperator;
import org.middleheaven.util.classification.LogicOperator;
import org.middleheaven.util.collections.Interval;


public class CriteriaInterpreter {

	private DataBaseDialect dataBaseDialect;
	private Criteria<?> criteria;
	private StorableModelReader reader;
	private StorableEntityModel model;

	public CriteriaInterpreter(DataBaseDialect dataBaseDialect,Criteria<?> criteria, StorableModelReader reader) {
		this.dataBaseDialect = dataBaseDialect;
		this.criteria = criteria;
		this.reader = reader;
		this.model = reader.read(criteria.getTargetClass());
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
		List<ColumnValueHolder> params = new LinkedList<ColumnValueHolder>();

		String alias = dataBaseDialect.aliasFor(model.getEntityHardName(),false);

		// LIMITS: distinct and  top n 
		writeStartLimitClause(sqlBuilder);

		// RETURN CLAUSE
		writeResultColumnsClause(alias,sqlBuilder);

		// FROM CLAUSE 
		writeFromClause(alias,sqlBuilder);

		// JOIN CLAUSE
		writeJoinClause(alias, sqlBuilder);

		// WHERE CLAUSE
		writeWhereClause(alias,sqlBuilder,params);

		// GROUP BY
		writeGroupByClause(alias,sqlBuilder);

		// ORDER BY
		writeOrderByClause(alias,sqlBuilder);


		// LIMITS: Limit and offset
		writeEndLimitClause(sqlBuilder);

		return new SQLRetriveCommand(dialect(),sqlBuilder.toString(),params);
	}



	public DataBaseCommand translateDelete(){
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ");

		dialect().writeEnclosureHardname(sqlBuilder, model.getEntityHardName());


		List<ColumnValueHolder> params = new LinkedList<ColumnValueHolder>();

		// WHERE CLAUSE
		writeWhereClause(null, sqlBuilder,params);

		return new SQLDeleteCommand(dialect(),sqlBuilder.toString(),params);
	}


	protected void writeStartLimitClause(StringBuilder selectBuffer) {
		if (criteria.isDistinct()){
			selectBuffer.append(" DISTINCT ");
		} 
	}



	protected void writeResultColumnsClause (String alias, StringBuilder queryBuffer){

		if (this.criteria().isCountOnly()){

			queryBuffer.append("COUNT(");

			dialect().writeQueryHardname(queryBuffer,dataBaseDialect.aliasFor(model().identityFieldModel().getHardName(), alias));

			queryBuffer.append(")");

		} else if (this.criteria().projection() !=null){ // if this is a projection criteria 

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
				queryBuffer.append(' ');
				this.writeAlias(queryBuffer, dataBaseDialect.aliasFor(model.getEntityHardName(),false));
				queryBuffer.append(".* ");
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

	protected void writeFromClause(String alias , StringBuilder queryBuffer){

		// FROM ClAUSE
		queryBuffer.append(" FROM ")
		.append(dialect().startDelimiter())
		.append(model.getEntityHardName().toLowerCase())
		.append(dialect().endDelimiter());

		if (alias!=null){
			writeAliasSeparator(queryBuffer); 
			writeAlias(queryBuffer,alias);
		}
	}

	protected void writeAliasSeparator(StringBuilder queryBuffer){
		queryBuffer.append(" AS ");
	}

	protected void writeAlias(StringBuilder queryBuffer , String alias){
		queryBuffer.append(alias);
	}	

	private void sweepJunctionCriterions(List<JunctionCriterion> criterions ,Collection<Criterion> criterias ){

		for (Iterator<Criterion> it = criterias.iterator(); it.hasNext();){
			Criterion c = (Criterion)it.next();
			if (c instanceof JunctionCriterion){
				JunctionCriterion junction = (JunctionCriterion)c;
				if ( junction.getSubCriteria() != null){
					criterions.add(junction);
					sweepJunctionCriterions( criterions, 
							junction.getSubCriteria().constraints().reduce().criterias()
					);
				}
			}
		}

	}

	protected void writeJoinClause(String mainAlias, StringBuilder joinClause) {


		Collection<Criterion> allCriterion = criteria().constraints().reduce().criterias();
		if (!allCriterion.isEmpty()){
			List<JunctionCriterion> junctionCriterions = new LinkedList<JunctionCriterion>();

			// recolhe todos os criterios de join
			sweepJunctionCriterions (junctionCriterions, allCriterion);

			// apenas processa os criterios do tipo JuntionRestriction, se algum
			for (Iterator<JunctionCriterion> it = junctionCriterions.iterator(); it.hasNext();){

				JunctionCriterion jr = it.next();

				StorableEntityModel sourceModel = reader.read(jr.getSourceType());
				StorableEntityModel targetModel = reader.read(jr.getTargetType());

				StorableFieldModel fm = sourceModel.fieldModel(jr.getFieldName());

				if (fm.getDataType().isToOneReference()){
					// join
					QualifiedName kfm = targetModel.identityFieldModel().getHardName();

					String targetAlias = dataBaseDialect.aliasFor(targetModel.getEntityHardName(),true);
					String sourceAlias = dataBaseDialect.aliasFor(sourceModel.getEntityHardName(),false);


					jr.setAlias(targetAlias);

					joinClause.append(" INNER JOIN ");

					dialect().writeJoinTableHardname(joinClause, targetModel.getEntityHardName());

					writeAliasSeparator(joinClause);
					writeAlias(joinClause,targetAlias);

					joinClause.append(" ON ");

					dialect().writeJoinField(joinClause, sourceAlias, fm.getHardName().getName());

					joinClause.append(" = ");

					dialect().writeJoinField(joinClause, targetAlias, kfm.getName());



					// do not write WHERE clause because it will been writen in the normal process
				} 
			}
		}
	}

	protected void writeOrderByClause(String alias, StringBuilder queryBuffer){

		// do not write order by order by if its a count query
		if (queryBuffer.indexOf("COUNT(")>=0){
			return;
		}

		// JOIN Order By
		List<Criterion> list = new ArrayList<Criterion>();

		list.add(this.criteria.constraints());

		popJunctionCriterions(list, 0);


		if (list.isEmpty()){
			// simple ordering
			if (!criteria().ordering().isEmpty()){
				queryBuffer.append(" ORDER BY ");

				boolean first = true;
				// result table level
				for (OrderingCriterion criterion  : criteria().ordering()){

					if (!first){
						queryBuffer.append(", ");
					}
					first=false;

					writeOrdeByClauseElement(alias, queryBuffer, criterion);

				}
			}
		} else {

			// join ordering 

			Collection<AliasedOrderingCriterion> all = new LinkedList<AliasedOrderingCriterion>();

			for (OrderingCriterion oc : criteria().ordering()){
				all.add(new AliasedOrderingCriterion(alias, oc));
			}

			for (Criterion c : list){

				JunctionCriterion jc = ((JunctionCriterion)c);

				for (OrderingCriterion oc  : jc.getSubCriteria().ordering()){

					all.add(new AliasedOrderingCriterion(alias, oc));

				}

			}


			if (!all.isEmpty()){
				queryBuffer.append(" ORDER BY ");

				boolean first = true;
				// result table level
				for (AliasedOrderingCriterion aoc  : all){

					if (!first){
						queryBuffer.append(", ");
					}
					first=false;

					writeOrdeByClauseElement(aoc.alias, queryBuffer, aoc.criterion);

				}

			}
		}



	}

	private class AliasedOrderingCriterion {

		public OrderingCriterion criterion;
		public String alias;

		public AliasedOrderingCriterion(String alias , OrderingCriterion criterion) {
			this.criterion = criterion;
			this.alias = alias;
		}

	}

	private void popJunctionCriterions (List<Criterion> stack, int index){
		if(stack.isEmpty()){
			return;
		}

		Criterion c = stack.get(index);
		if (c instanceof LogicCriterion){
			stack.remove(index);
			for (Criterion b : ((LogicCriterion)c).criterias()){
				if (b instanceof JunctionCriterion){
					stack.add(b);
				}
			}
		} else if (c instanceof FieldCriterion){
			stack.remove(index);
		} else if (c instanceof JunctionCriterion){
			popJunctionCriterions(stack, index+1);
		}


	}

	protected void writeOrdeByClauseElement(String alias, StringBuilder queryBuffer, OrderingCriterion criterion){

		dialect().writeQueryHardname(queryBuffer,dataBaseDialect.aliasFor( model.fieldModel(criterion.getFieldName()).getHardName(), alias));

		queryBuffer.append(criterion.isDescendant()?" desc":" asc");
	}

	protected void writeGroupByClause(String alias, StringBuilder queryBuffer){


		//GROUP BY

		Projection aggregation = criteria.projection();

		Collection<QualifiedName> aggregationGroups;
		if (aggregation ==null){
			aggregationGroups = Collections.emptySet();
		} else {
			aggregationGroups = aggregation.groups();
		}

		if (!aggregationGroups.isEmpty()){

			queryBuffer.append(" GROUP BY ");

			boolean first = true;
			// write the name of all groups from agregation
			for (Iterator<QualifiedName> it = aggregationGroups.iterator(); it.hasNext();){
				QualifiedName name = it.next();

				if (!first){
					queryBuffer.append(" , ");
				}
				first = false;

				dialect().writeQueryHardname(queryBuffer,  model().fieldModel(name).getHardName());

			}

		}


	}

	protected void writeWhereClause(String alias, StringBuilder queryBuffer, List<ColumnValueHolder> params  ){
		// WHERE CLAUSE
		// Cria primeiro a sentença. 
		// Se não houver nenhum o where não é adicionado
		if ( criteria().constraints().criteriaCount()==0){
			return;
		}

		StringBuilder whereClause =  new StringBuilder();

		translateCriteriaToWhereClause(alias, whereClause,params, criteria().constraints().simplify(), model());

		if (whereClause.length()>2){
			queryBuffer.append(" WHERE ");
			queryBuffer.append(whereClause);
		}

	}



	protected void translateFieldInQueryCriteriaToEmptySetInClause(StringBuilder criteriaBuffer,FieldInSetCriterion criterion,StorableEntityModel model){
		// [NOT] IN ( foo ) 
		// where foo is return nothing select 
		// made using range(0)
		Criteria<?> emptyCriteria =  CriteriaBuilder.search(criteria.getTargetClass()).limit(0).all();

		emptyCriteria.setKeyOnly(true);

		CriteriaInterpreter ci = dialect().newCriteriaInterpreter(criteria, reader);

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

	protected void translateFieldInQueryCriteriaToWhereClause(StringBuilder criteriaBuffer,FieldInSetCriterion criterion,StorableEntityModel  model){

		// [NOT] IN ( Select field FROM table ... ) 
		Criteria<?> c = (Criteria<?>)criterion.valueHolder().getValue();
		c.setDistinct(true);

		CriteriaInterpreter ci = dialect().newCriteriaInterpreter(c, reader);

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






	protected  void translateCriteriaToWhereClause(String alias , StringBuilder criteriaBuffer, List<ColumnValueHolder> params,Criterion criterion ,StorableEntityModel model ){

		if (criterion instanceof EmptyCriterion){
			criteriaBuffer.append("TRUE");
		} else if (criterion instanceof JunctionCriterion ) {

			JunctionCriterion jr = (JunctionCriterion)criterion;


			if (!jr.getSubCriteria().constraints().isEmpty()){
				if (criteriaBuffer.toString().trim().length()>0){
					criteriaBuffer.append(" AND ");
				}

				translateCriteriaToWhereClause(jr.getAlias(), criteriaBuffer, params, 
						jr.getSubCriteria().constraints(), reader.read(jr.getTargetType())
				);
			}


		}else if (criterion instanceof FieldInSetCriterion){
			FieldInSetCriterion f = (FieldInSetCriterion)criterion;

			if (f.useCriteria()){
				// values are obtain in a sub query
				translateFieldInQueryCriteriaToWhereClause(criteriaBuffer , f , model);
			} else {
				// values are obtain explicitly. use IN () command

				if (f.valueHolder().isEmpty()){

					translateFieldInQueryCriteriaToEmptySetInClause(criteriaBuffer , f , model);

				} else {
					dialect().writeQueryHardname(criteriaBuffer, dataBaseDialect.aliasFor(model.fieldModel(f.getFieldName()).getHardName(), alias) );

					if (!f.isIncluded()){
						criteriaBuffer.append(" NOT ");
					}
					criteriaBuffer.append(" IN (");
					StorableFieldModel fm = model.fieldModel(f.getFieldName());

					Collection<Object> values = (Collection<Object>) f.valueHolder().getValue();
					for (Iterator<Object> it = values.iterator(); it.hasNext();){
						// read the real datatype from the model
						params.add(new ColumnValueHolder(it.next(), fm.getDataType()));
						criteriaBuffer.append("?,");
					}

					criteriaBuffer.delete(criteriaBuffer.length()-1, criteriaBuffer.length());
					criteriaBuffer.append(")");
				}
			}
		}else if (criterion instanceof FieldCriterion){
			FieldCriterion f = (FieldCriterion)criterion;

			StorableFieldModel fm = model.fieldModel(f.getFieldName());
			if (fm==null){
				throw new IllegalStateException(f.getFieldName() + " not found");
			}
			FieldValueHolder vholder = f.valueHolder();

			if (vholder.getDataType()==null){
				vholder.setDataType(fm.getDataType());
			}

			if (!fm.isTransient() && !fm.isVersion() && !fm.getDataType().isVirtual()){

				criteriaBuffer.append(' ');
				CriterionOperator op = f.getOperator();

				if (CriterionOperator.CONTAINS.equals(op) || 
						CriterionOperator.STARTS_WITH.equals(op) || 
						CriterionOperator.ENDS_WITH.equals(op) ){

					writeLikeClause (fm, criteriaBuffer ,false, op,alias);
					params.add(new ColumnValueHolder(vholder, op));

				} else if (CriterionOperator.IS_NULL.equals(op)){
					dialect().writeQueryHardname(criteriaBuffer, dataBaseDialect.aliasFor(fm.getHardName(),alias));

					if (op.isNegated()){
						criteriaBuffer.append(" IS NOT NULL ");
					} else { // positive operators
						criteriaBuffer.append(" IS NULL ");
					}
					// do not add parameter as there is no value holder
				} else if (CriterionOperator.IN.equals(op)){

					if (vholder.getValue() instanceof Collection){
						dialect().writeQueryHardname(criteriaBuffer, dataBaseDialect.aliasFor(fm.getHardName(),alias));


						if (op.isNegated()){
							criteriaBuffer.append(" NOT");
						} 
						criteriaBuffer.append(" IN (");


						Collection collection = (Collection) vholder.getValue();

						for (Object obj : collection){
							params.add(new ColumnValueHolder(obj,vholder.getDataType()));
							criteriaBuffer.append(" ?, ");
						}
						criteriaBuffer.delete(criteriaBuffer.length()-2, criteriaBuffer.length());
						criteriaBuffer.append(") ");
					} else if (vholder.getValue() instanceof Interval){

						Interval interval =  (Interval)vholder.getValue();

						criteriaBuffer.append("(");

						if (interval.start()!=null){
							dialect().writeQueryHardname(criteriaBuffer, dataBaseDialect.aliasFor(fm.getHardName(),alias));
							if (op.isNegated()){
								criteriaBuffer.append(" < ? ");
							} else {
								criteriaBuffer.append(" >= ? ");
							}
							params.add(new ColumnValueHolder(interval.start(),vholder.getDataType()));
						}

						if (interval.end()!=null){

							if (op.isNegated()){
								if (interval.start()!=null){
									criteriaBuffer.append(" OR ");
								}
								dialect().writeQueryHardname(criteriaBuffer, dataBaseDialect.aliasFor(fm.getHardName(),alias));
								criteriaBuffer.append(" > ? ");
							} else {
								if (interval.start()!=null){
									criteriaBuffer.append(" AND ");
								}
								dialect().writeQueryHardname(criteriaBuffer, dataBaseDialect.aliasFor(fm.getHardName(),alias));
								criteriaBuffer.append(" <= ? ");
							}
							params.add(new ColumnValueHolder(interval.end(),vholder.getDataType()));
						}

						criteriaBuffer.append(") ");

					} else {
						throw new UnsupportedOperationException(vholder.getValue() + " is not supported for IN clause");
					}

				} else {
					dialect().writeQueryHardname(criteriaBuffer, dataBaseDialect.aliasFor(fm.getHardName(),alias));

					if (op.isNegated()){

						if (CriterionOperator.GREATER_THAN.equals(op)){
							criteriaBuffer.append(" <= ? ");
						} else if (CriterionOperator.GREATER_THAN_OR_EQUAL.equals(op)){
							criteriaBuffer.append(" < ? ");
						}  else if (CriterionOperator.LESS_THAN.equals(op)){
							criteriaBuffer.append(" >= ? ");
						}  else if (CriterionOperator.LESS_THAN_OR_EQUAL.equals(op)){
							criteriaBuffer.append(" > ? ");
						} else if (CriterionOperator.EQUAL.equals(op)){
							criteriaBuffer.append(" <> ? ");
						} else if (CriterionOperator.IS_NULL.equals(op)){
							criteriaBuffer.append(" IS NOT NULL ");
						}  else {
							throw new RuntimeException("Unkown operator " + op);
						} 
					} else { // positive operators

						if (CriterionOperator.GREATER_THAN.equals(op)){
							criteriaBuffer.append(" > ? ");
						} else if (CriterionOperator.GREATER_THAN_OR_EQUAL.equals(op)){
							criteriaBuffer.append(" >= ? ");
						}  else if (CriterionOperator.LESS_THAN.equals(op)){
							criteriaBuffer.append(" < ? ");
						}  else if (CriterionOperator.LESS_THAN_OR_EQUAL.equals(op)){
							criteriaBuffer.append(" <= ? ");
						}  else if (CriterionOperator.EQUAL.equals(op)){
							criteriaBuffer.append(" = ? ");
						} else if (CriterionOperator.IS_NULL.equals(op)){
							criteriaBuffer.append(" IS NULL ");
						} else {
							throw new RuntimeException("Unkown operator " + op);
						}
					}
					params.add(new ColumnValueHolder(vholder,op));
				}



			}
		}else if (criterion instanceof LogicCriterion){
			Collection<Criterion> criterias = ((LogicCriterion)criterion).criterias();
			if (!criterias.isEmpty()){

				// primeiro processa os criterios do tipo JuntionRestriction, se algum
				List<Criterion> others = new ArrayList<Criterion>(criterias.size());
				for (Iterator<Criterion> it = criterias.iterator(); it.hasNext();){
					Criterion c = (Criterion)it.next();
					if (c instanceof JunctionCriterion){
						translateCriteriaToWhereClause(alias,criteriaBuffer,params,c,model);
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
						translateCriteriaToWhereClause(alias,criteriaBuffer,params,c,model);
						writeOperator = (criteriaBuffer.length()>len);
					}
					criteriaBuffer.append(')');
				}
			}
		} 
	}


	protected void writeLikeClause(StorableFieldModel fm, StringBuilder criteriaBuffer, boolean caseSensitive, CriterionOperator op,String alias) {
		dialect().writeQueryHardname(criteriaBuffer, dataBaseDialect.aliasFor(fm.getHardName(),alias));

		if (op.isNegated()){
			criteriaBuffer.append(" NOT");
		}
		criteriaBuffer.append(" LIKE ? ");
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
			selectBuffer.append(n.getName().toLowerCase());
		} else if (op instanceof MaxFieldOperator){
			selectBuffer.append("MAX(");
			QualifiedName n = ((MaxFieldOperator)op).getFieldName();
			dialect().writeQueryHardname(selectBuffer, this.model().fieldModel(n).getHardName());
			selectBuffer.append(") AS ");
			selectBuffer.append(n.getName().toLowerCase());
		} else if (op instanceof MinFieldOperator){
			selectBuffer.append("MIN(");
			QualifiedName n = ((MinFieldOperator)op).getFieldName();
			dialect().writeQueryHardname(selectBuffer, this.model().fieldModel(n).getHardName());
			selectBuffer.append(") AS ");
			selectBuffer.append(n.getName().toLowerCase());
		} else {
			throw new StorageException("ProjectionOperator " + op.getClass().getName() + " is not supported by " + this.getClass().getName());
		}
	}

}
