package org.middleheaven.domain.store.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.middleheaven.core.metaclass.MetaBean;
import org.middleheaven.core.metaclass.ReflectionBean;
import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.criteria.EqualsOtherInstanceCriterion;
import org.middleheaven.domain.criteria.FieldJuntionCriterion;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.model.ReferenceDataTypeModel;
import org.middleheaven.domain.query.QueryResult;
import org.middleheaven.domain.store.AbstractEntityInstanceStorage;
import org.middleheaven.domain.store.EntityInstance;
import org.middleheaven.domain.store.InstanceStorageException;
import org.middleheaven.persistance.DataColumn;
import org.middleheaven.persistance.DataQuery;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.DataService;
import org.middleheaven.persistance.DataSet;
import org.middleheaven.persistance.DataSetNotFoundException;
import org.middleheaven.persistance.DataStoreSchema;
import org.middleheaven.persistance.DataStoreSchemaName;
import org.middleheaven.persistance.RelatedDataSet;
import org.middleheaven.persistance.criteria.DataSetCriteria;
import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.criteria.ResultColumnDefinition;
import org.middleheaven.persistance.criteria.building.ColumnNameValueLocator;
import org.middleheaven.persistance.criteria.building.ColumnOrderConstraint;
import org.middleheaven.persistance.criteria.building.ColumnValueConstraint;
import org.middleheaven.persistance.criteria.building.ExplicitValueLocator;
import org.middleheaven.persistance.criteria.building.OrderConstraint;
import org.middleheaven.persistance.criteria.building.RelationOperator;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.persistance.model.DataSetModel;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.classification.LogicOperator;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.CriterionOperator;
import org.middleheaven.util.criteria.FieldValueCriterion;
import org.middleheaven.util.criteria.JunctionCriterion;
import org.middleheaven.util.criteria.LogicCriterion;
import org.middleheaven.util.criteria.OrderingCriterion;
import org.middleheaven.util.criteria.ReadStrategy;
import org.middleheaven.util.criteria.SingleObjectValueHolder;

/**
 * Realizes a EntityInstanceStorage on top of a dataset oriented persistence mechanism.
 */
public class DataSetEntityInstanceStorage extends AbstractEntityInstanceStorage {


	private DataService dataPersistanceService;
	private DomainModel domaiModel;
	private DomainModelDataSetTypeMapper Function;

	/**
	 * 
	 * Constructor.
	 * @param dataPersistanceService
	 * @param domainModel
	 * @param Function
	 */
	public DataSetEntityInstanceStorage (DataService dataPersistanceService , DomainModel domainModel, DomainModelDataSetTypeMapper Function){
		this.dataPersistanceService = dataPersistanceService;
		this.domaiModel = domainModel;
		this.Function = Function;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public <T> QueryResult<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy) {
		try {

			EntityModel entityModel = this.getStoreManager().getDomainModel().getModelFor(criteria.getTargetClass().getName());

			final EntityModelDataSetMapping mapping = Function.getMappingFor(entityModel);

			DataStoreSchemaName schemaName = mapping.getSchemaName();

			DataStoreSchema schema = dataPersistanceService.getDataStoreSchema(schemaName);

			// translate criteria to a DataSetCriteria
			DataQuery dq = schema.query(interpret(criteria, strategy));


			return new DataQueryAdapter<T>(mapping, dq);
		} catch (Exception e){
			throw this.handleException(e);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public <T> void remove(EntityCriteria<T> criteria) {
		try {
			EntityModel entityModel = this.getStoreManager().getDomainModel().getModelFor(criteria.getTargetClass().getName());

			final EntityModelDataSetMapping mapping = Function.getMappingFor(entityModel);

			DataStoreSchemaName schemaName = mapping.getSchemaName();

			DataStoreSchema schema = dataPersistanceService.getDataStoreSchema(schemaName);

			if (mapping.isSingleDataSetInheritance()){
				schema.getDataSet(mapping.getDataSetName()).delete(interpret(criteria, ReadStrategy.fowardReadOnly()));
			} else {
				throw new UnsupportedOperationException("Not implememented yet");
			}

		} catch (Exception e){
			throw this.handleException(e);
		}

	}

	/**
	 * @param e
	 * @return
	 */
	private InstanceStorageException handleException(Exception e) {
		return new InstanceStorageException(e);
	}

	private <T> DataSetCriteria interpret(EntityCriteria<T> criteria, ReadStrategy strategy){


		DataSetCriteria dsCriteria = new DataSetCriteria();

		dsCriteria.setDistinct(criteria.isDistinct());
		dsCriteria.setMaxCount(criteria.getCount());
		dsCriteria.setOffset(criteria.getStart());

		interpretLogic(dsCriteria , criteria);
		interpretGroup(dsCriteria , criteria);
		interpretOrder(dsCriteria , criteria);


		EntityModel baseModel = domaiModel.getModelFor(criteria.getTargetClass().getName());

		EntityModelDataSetMapping sourceMapping = this.Function.getMappingFor(baseModel);


		List<RelatedDataSet> relatedDS = new LinkedList<RelatedDataSet>();

		for ( DataColumnModel cm : sourceMapping.getTypeMapper().getColumns()){
			// add the column tot re resulting projection
			dsCriteria.addResultColumn(new ResultColumnDefinition(cm.getName()));

		}

		// add the DS corresponding with the searched entity it self

		Collection<DataSetModel> baseDataSets = this.resolveDataSetModelsFromMapping(sourceMapping);

		for (DataSetModel dsModel : baseDataSets){
			RelatedDataSet rd = new RelatedDataSet( new LogicConstraint(LogicOperator.and()), RelationOperator.INNER_JOIN);

			rd.setSourceDataSetModel(dsModel);

			relatedDS.add(rd);
		}

		// resolve related models based on the target entity strutcture
		// this will be used for joins and return fields
		resolveRelatedAndProjection(baseModel, relatedDS, 0 , strategy.getMaxFetchDeep(), dsCriteria);



		for (RelatedDataSet rd : relatedDS){

			if (rd.getTargetDataSetModel() != null){
				for (DataColumnModel cm :rd.getTargetDataSetModel().getModelColumns()){
					// add the column to the resulting projection
					dsCriteria.addResultColumn(new ResultColumnDefinition(cm.getName()));
				}
			}
		}

		// resolve related models based on the where clause.

		resolveRelatedByWhere(criteria.constraints(), relatedDS, baseDataSets);


		for (RelatedDataSet rd : relatedDS){
			// add the dataset to the criteria.
			dsCriteria.addRelatedDataSet(rd);

		}
		return dsCriteria;
	}

	/**
	 * @param constraints
	 * @param relatedDS
	 */
	private void resolveRelatedByWhere(LogicCriterion constraints, List<RelatedDataSet> relatedDS, Collection<DataSetModel> baseDataSets ) {
		for (Criterion c : constraints){
			if (c instanceof LogicCriterion){
				resolveRelatedByWhere((LogicCriterion)c, relatedDS, baseDataSets);
			} else if (c instanceof JunctionCriterion){
				JunctionCriterion junction = (JunctionCriterion) c;

				EntityModel joinModel = domaiModel.getModelFor(junction.getTargetType().getName());


				EntityModelDataSetMapping sourceMapping = this.Function.getMappingFor(joinModel);

				Collection<DataSetModel> targetDataSets = resolveDataSetModelsFromMapping(sourceMapping);

				for (DataSetModel target : targetDataSets){
					for (DataSetModel source : baseDataSets){
						RelatedDataSet rd = new RelatedDataSet( new LogicConstraint(LogicOperator.and()), RelationOperator.INNER_JOIN);

						rd.setSourceDataSetModel(source);
						rd.setTargetDataSetModel(target);

						QualifiedName name = Function.getEntityFieldTypeMapper(
								junction.getFieldName()
						).getColumns()[0].getName(); // 0 == key ?

						ColumnValueConstraint cc = new ColumnValueConstraint(
								new ColumnNameValueLocator(name),
								CriterionOperator.EQUAL,
								new ColumnNameValueLocator(target.getPrimaryKeyColumns().iterator().next().getName())
								);

						rd.getRelationConstraint().addConstraint(cc);

						relatedDS.add(rd);
					}
				}

			}
		}
	}

	private Collection<DataSetModel>  resolveDataSetModelsFromMapping(EntityModelDataSetMapping sourceMapping){

		Set<String> tablesAdded = new HashSet<String>();

		// an entity could have more than one dataset, althought normally it has one
		Collection<DataSetModel> baseDataSets = new ArrayList<DataSetModel>(1);

		for ( DataColumnModel cm : sourceMapping.getTypeMapper().getColumns()){

			// an entity could have more than one dataset
			if(tablesAdded.add(cm.getDataSetModel().getName())){
				baseDataSets.add(cm.getDataSetModel());
			}
		}

		return baseDataSets;
	}


	private void resolveRelatedAndProjection(EntityModel baseModel, List<RelatedDataSet> relatedDS, int currentLevel, int maxLevel, DataSetCriteria dsCriteria) {

		for (EntityFieldModel fm : baseModel.fields()){
			if (!fm.isTransient()){
				EntityFieldTypeMapper currentFieldFunction = Function.getEntityFieldTypeMapper(fm.getName());
				DataColumnModel currentFieldColumn = currentFieldFunction.getColumns()[0];

				if (fm.getDataType().isToOneReference()){

					ReferenceDataTypeModel refModel = (ReferenceDataTypeModel)fm.getDataTypeModel();

				
					// TODO multicolumn identifier

				
					EntityModel targetModel = domaiModel.getModelFor(refModel.getTargetType().getName());

					EntityFieldTypeMapper targetFieldFunction = Function.getEntityFieldTypeMapper(targetModel.identityFieldModel().getName());

					DataColumnModel targetFieldColumn = targetFieldFunction.getColumns()[0];

					RelatedDataSet rd = new RelatedDataSet( new LogicConstraint(LogicOperator.and()), RelationOperator.INNER_JOIN);

					rd.setSourceDataSetModel(currentFieldColumn.getDataSetModel());
					rd.setTargetDataSetModel(targetFieldColumn.getDataSetModel());

					ColumnValueConstraint cc = new ColumnValueConstraint(
							new ColumnNameValueLocator(currentFieldColumn.getName()),
							CriterionOperator.EQUAL,
							new ColumnNameValueLocator(targetFieldColumn.getName())
							);

					rd.getRelationConstraint().addConstraint(cc);

					relatedDS.add(rd);

					// continue the relation tree
					if (currentLevel == maxLevel){ // if is last level
						// use the source keys as it where target keys
						// this should render to source.sourcefield as target_tagetField
						ResultColumnDefinition colDef = new ResultColumnDefinition(currentFieldColumn.getName());
						colDef.setAlias(targetFieldColumn.getName().getQualifier() + "_" + targetFieldColumn.getName().getDesignation());
						dsCriteria.addResultColumn(colDef);

					} else if(currentLevel < maxLevel) {
						resolveRelatedAndProjection(targetModel, relatedDS , currentLevel + 1, maxLevel, dsCriteria);
					}

				} 
				
				dsCriteria.addResultColumn(new ResultColumnDefinition(currentFieldColumn.getName()));
				
			}
		}


		}


		private <T> void interpretOrder(DataSetCriteria dsCriteria, EntityCriteria<T> criteria){

			for (OrderingCriterion ordering : criteria.ordering()){

				DataColumnModel[] columns = Function.getEntityFieldTypeMapper(ordering.getFieldName()).getColumns();

				OrderConstraint orderConstraint = new ColumnOrderConstraint(columns[0].getName() , !ordering.isDescendant());

				dsCriteria.addOrderingConstraint(orderConstraint);

			}

		}

		private <T> void interpretGroup(DataSetCriteria dsCriteria, EntityCriteria<T> criteria){

		}

		private <T> void interpretLogic(DataSetCriteria dsCriteria, EntityCriteria<T> criteria){


			interpretLogicConstraint(dsCriteria, criteria.constraints());
		}


		private <T> void interpretLogicConstraint(DataSetCriteria dsCriteria, LogicCriterion criterion){

			final LogicConstraint constraint  = new LogicConstraint(criterion.getOperator());
			dsCriteria.addLogicConstraint(constraint);

			for (Criterion c : criterion){
				if (c instanceof FieldValueCriterion) {

					final FieldValueCriterion f = (FieldValueCriterion)c;

					interpretFieldValueCriterion(constraint, f);

				} else if (c instanceof LogicCriterion) {
					interpretLogicConstraint(dsCriteria, (LogicCriterion) c);	
				} else if (c instanceof FieldJuntionCriterion) {				
					interpretLogicConstraint(dsCriteria, ((FieldJuntionCriterion) c).getSubCriteria().constraints());
				} else if (c instanceof EqualsOtherInstanceCriterion){
					// the candidate has the same identity as the object in the criterion
					final EqualsOtherInstanceCriterion equalsOtherInstanceCriterion = (EqualsOtherInstanceCriterion) c;
					Object instance = equalsOtherInstanceCriterion.getInstance();
					Class<?> type = equalsOtherInstanceCriterion.getType();

					EntityModel entityModel = this.domaiModel.getModelFor(type.getName());

					EntityFieldModel idModel = entityModel.identityFieldModel();

					Object idValue = null;
					if (instance != null){
						MetaBean bean = new ReflectionBean(instance);

						idValue = bean.get(idModel.getName().getDesignation());
					} 

					FieldValueCriterion f = new FieldValueCriterion(
							idModel.getName(), 
							CriterionOperator.EQUAL, 
							new SingleObjectValueHolder(idValue)
					);

					interpretFieldValueCriterion(constraint, f);	
				} else {
					throw new IllegalStateException("Cannot interpret criterion " + c);
				}
			}

		}

		private void interpretFieldValueCriterion(final LogicConstraint constraint,
				final FieldValueCriterion f) {
			
			final EntityFieldTypeMapper entityFieldTypeMapper = Function.getEntityFieldTypeMapper(f.getFieldName());
			
			if (entityFieldTypeMapper == null){
				throw new IllegalModelStateException("There is not field " + f.getFieldName());
			}
			final DataRow row = new ConstraintDataRow(constraint, f);

			entityFieldTypeMapper.write(null, f.valueHolder().getValue(), row, entityFieldTypeMapper.getColumns());
		}

		private Map<DataSet , Collection<DataRow>> classify (Collection<EntityInstance> objs) throws DataSetNotFoundException{
			Map<DataSet , Collection<DataRow>> groups = new HashMap<DataSet , Collection<DataRow>>();

			for (EntityInstance i : objs) {

				EntityModel entityModel = i.getEntityModel();

				final EntityModelDataSetMapping mapping = Function.getMappingFor(entityModel);

				DataStoreSchema schema = dataPersistanceService.getDataStoreSchema(mapping.getSchemaName());

				DataSet dataSet =	schema.getDataSet(mapping.getDataSetName());

				for (DataRow row : mapping.write(i)) {

					Collection<DataRow> rows = groups.get(dataSet);
					if ( rows == null){
						rows = new LinkedList<DataRow>();
						groups.put(dataSet, rows);
					}

					rows.add(row);
				}
			}

			return groups;

		}

		@Override
		public void update(Collection<EntityInstance> objs) {
			try{
				for (Map.Entry<DataSet , Collection<DataRow>> entry : classify(objs).entrySet()) {
					entry.getKey().update(entry.getValue());
				}
			} catch (Exception e){
				throw this.handleException(e);
			}

		}

		@Override
		public void insert(Collection<EntityInstance> objs) {
			try{
				for (Map.Entry<DataSet , Collection<DataRow>> entry : classify(objs).entrySet()) {
					entry.getKey().insert(entry.getValue());
				}
			} catch (Exception e){
				throw this.handleException(e);
			}
		}

		@Override
		public void remove(Collection<EntityInstance> objs) {
			try{
				for (Map.Entry<DataSet , Collection<DataRow>> entry : classify(objs).entrySet()) {
					entry.getKey().insert(entry.getValue());
				}
			} catch (Exception e){
				throw this.handleException(e);
			}
		}


		protected Sequence<Long> getSeedSequence(EntityInstance instance){
			final EntityModelDataSetMapping mapping = Function.getMappingFor(instance.getEntityModel());

			return dataPersistanceService.getDataStoreSchema(mapping.getSchemaName()).getSequence(mapping.getDataSetName());

		}
		
		
		private static class ConstraintDataRow implements DataRow {

			private LogicConstraint constraint;
			private FieldValueCriterion f;

			/**
			 * Constructor.
			 * @param constraint
			 * @param f 
			 */
			public ConstraintDataRow(LogicConstraint constraint, FieldValueCriterion f) {
				this.constraint = constraint;
				this.f = f;
			}

			@Override
			public Iterator<DataColumn> iterator() {
				throw new UnsupportedOperationException("Not implememented yet");
			}

			@Override
			public DataColumn getColumn(final QualifiedName name) {
				return new DataColumn(){

					@Override
					public DataColumnModel getModel() {
						throw new UnsupportedOperationException("Not implememented yet");
					}

					@Override
					public Object getValue() {
						throw new UnsupportedOperationException("Not implememented yet");
					}

					@Override
					public void setValue(Object value) {
						ColumnValueConstraint v = new ColumnValueConstraint(
								new ColumnNameValueLocator(name),
								f.getOperator(),
								new ExplicitValueLocator(value)
								);

						constraint.addConstraint(v);
					}

				};

			}

		}

	}
