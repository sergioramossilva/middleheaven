/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.model.annotations.Id;
import org.middleheaven.model.annotations.NotNull;
import org.middleheaven.model.annotations.Transient;
import org.middleheaven.model.annotations.Version;
import org.middleheaven.model.annotations.mapping.Column;
import org.middleheaven.model.annotations.mapping.Columns;
import org.middleheaven.model.annotations.mapping.Dataset;
import org.middleheaven.model.annotations.mapping.DatasetInheritance;
import org.middleheaven.model.annotations.mapping.Type;
import org.middleheaven.persistance.DataStoreSchemaName;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.model.ColumnType;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.persistance.model.DataColumnModelBean;
import org.middleheaven.persistance.model.EditableDataSet;
import org.middleheaven.storage.StorableEnum;
import org.middleheaven.storage.types.BooleanTypeMapper;
import org.middleheaven.storage.types.CalendarDateTimeTypeMapper;
import org.middleheaven.storage.types.CalendarDateTypeMapper;
import org.middleheaven.storage.types.CurrencyTypeMapper;
import org.middleheaven.storage.types.EntityFieldTypeMapper;
import org.middleheaven.storage.types.EntityInstanceTypeMapper;
import org.middleheaven.storage.types.EnumTypeMapper;
import org.middleheaven.storage.types.IdentityTypeMapper;
import org.middleheaven.storage.types.MoneyTypeMapper;
import org.middleheaven.storage.types.NumberTypeMapper;
import org.middleheaven.storage.types.RealTypeMapper;
import org.middleheaven.storage.types.StringTypeMapper;
import org.middleheaven.storage.types.TypeMapper;
import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class AnnotationDomainModelDataSetMapper implements DomainModelDataSetMapper {

	private final Map<String, TypeMapper> types = new HashMap<String, TypeMapper>();
	
	private final Map<String, EntityModelDataSetMapping> mappings = new HashMap<String, EntityModelDataSetMapping>();
	private final DataStoreSchemaName dataStoreSchemaName;
	private final DomainModel domainModel;


	/**
	 * 
	 * @param domainModel
	 * @param dataMapper 
	 * @return
	 */
	public static AnnotationDomainModelDataSetMapper newInstance(DataStoreSchemaName dataStoreSchemaName, DomainModel domainModel ){
		return new AnnotationDomainModelDataSetMapper(domainModel,dataStoreSchemaName);
	}

	private AnnotationDomainModelDataSetMapper(DomainModel domainModel, DataStoreSchemaName dataStoreSchemaName){
		this.dataStoreSchemaName = dataStoreSchemaName;
		this.domainModel = domainModel;
		
		this.registerTypeMapper(new NumberTypeMapper(Integer.class));
		this.registerTypeMapper(new NumberTypeMapper(Long.class));
		this.registerTypeMapper(new NumberTypeMapper(BigDecimal.class));
		this.registerTypeMapper(new NumberTypeMapper(BigInteger.class));
		this.registerTypeMapper(new NumberTypeMapper(Short.class));
		this.registerTypeMapper(new NumberTypeMapper(Byte.class));
		this.registerTypeMapper(new BooleanTypeMapper());
		this.registerTypeMapper(new StringTypeMapper());
		this.registerTypeMapper(new CalendarDateTypeMapper());
		this.registerTypeMapper(new CalendarDateTimeTypeMapper());
		this.registerTypeMapper(new MoneyTypeMapper());
		this.registerTypeMapper(new CurrencyTypeMapper());
		this.registerTypeMapper(new RealTypeMapper());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityModelDataSetMapping getMappingFor(EntityModel entityModel) {

		return mappings.get(entityModel.getEntityClass().getName());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		
		// because of dependencies, a queue must be used
		Map<String, EntityModel> queue = new HashMap<String, EntityModel>();
		
		for (EntityModel model : domainModel.models()){

			queue.put(model.getEntityClass().getName(), model);

		}
		
		while(!queue.isEmpty()){

			processType(queue , queue.keySet().iterator().next());
		}
	}

	/**
	 * @param model
	 */
	private TypeMapper processType(Map<String, EntityModel> queue ,String typeName) {
		
		
		EntityModel model = queue.remove(typeName);
		
		
		EditableEntityModelDataSetMapping mapping = new EditableEntityModelDataSetMapping();

		mappings.put(model.getEntityClass().getName(), mapping);
		
		mapping.setDataStoreSchemaName(this.dataStoreSchemaName);

		readDataSetName(mapping , model);

		TypeMapper type = types.get(model.getEntityClass().getName());

		if (type == null){
			type = createEntityType(mapping , model, queue);
		}

		mapping.setTypeMapping((EntityInstanceTypeMapper) type);
		
		return type;
	}

	/**
	 * @param model
	 * @param queue 
	 * @return
	 */
	private EntityInstanceTypeMapper createEntityType(EditableEntityModelDataSetMapping mapping , EntityModel model, Map<String, EntityModel> queue) {

		EntityInstanceTypeMapper type = new EntityInstanceTypeMapper(model);

		mapping.addInstanceMapper(model.getEntityName(), type);
		
		EditableDataSet dsModel = new EditableDataSet(); // TODO
		
		dsModel.setName(mapping.getDataSetName());
		
		for (EntityFieldModel field : model.fields()){

			PropertyAccessor pa = model.getEntityClass().getPropertyAcessor(field.getName().getName());


			TypeMapper fieldType = readFieldTypeMapper(pa, field, queue);

			DataColumnModel[] columns = readColumns(dsModel, pa, field , mapping);
			
			
			
			EntityFieldTypeMapper fieldTypeMapper = new EntityFieldTypeMapper(field, fieldType, columns);

			
			
			type.addFielTypeMapper(fieldTypeMapper);



		}

		return type;

	}



	/**
	 * @param dsModel 
	 * @param pa
	 * @param field
	 * @param mapping 
	 */
	private DataColumnModel[] readColumns(EditableDataSet dsModel, PropertyAccessor pa, EntityFieldModel field, EditableEntityModelDataSetMapping mapping) {

		Column[] all;

		Columns columns = pa.getAnnotation(Columns.class);

		if (columns == null){

			Column column = pa.getAnnotation(Column.class);

			if (column == null){
				
				if (pa.isAnnotadedWith(Transient.class)) {
					all = new Column[0];
				} else {
					
					// auto mappping
					
					return columnBeanFromField(dsModel, field); 

				}
				
			} else {
				all = new Column[]{column};
			}

		} else {
			all = columns.columns();
		}

		DataColumnModel[] columnModels = new DataColumnModel[all.length];
		
		for (int i = 0 ; i < all.length; i++){
			
			DataColumnModelBean cm = new DataColumnModelBean();
					
			Column c = all[i];
			
			cm.setName(QualifiedName.qualify(dsModel.getName(), c.name()));
			cm.setDataSetModel(dsModel);
			cm.setNullable(!pa.isAnnotadedWith(NotNull.class));
			cm.setPrecision(c.precision());
			cm.setSize(c.length());
			cm.setType(c.type());
			cm.setVersion(!pa.isAnnotadedWith(Version.class));
			
		
			dsModel.addColumn(cm);
			columnModels[i] = cm;
		

		}
		
		return columnModels;
	}



	/**
	 * @param dsModel 
	 * @param field
	 * @return
	 */
	private DataColumnModel[] columnBeanFromField(EditableDataSet dsModel, EntityFieldModel field) {
		DataColumnModel[] bean = new DataColumnModel[1];
		
		DataColumnModelBean column = new DataColumnModelBean();
		
		column.setName(QualifiedName.qualify(dsModel.getName(), field.getName().getName()));
		column.setNullable(field.isNullable());
		column.setType(mapColumnTypeFromDataType(field.getDataType()));
		column.setVersion(field.isVersion());
		column.setDataSetModel(dsModel);
		
		dsModel.addColumn(column);
		bean[0] = column;
		return bean;
	}

	/**
	 * @param dataType
	 * @return
	 */
	private ColumnType mapColumnTypeFromDataType(DataType dataType) {
		switch (dataType){
		case DATE:
			return ColumnType.DATE;
		case DATETIME:
			return ColumnType.DATETIME;
		case DECIMAL:
			return ColumnType.DECIMAL;
		case STATUS:
		case ENUM:
			return ColumnType.SMALL_INTEGER;
		case INTEGER:
			return ColumnType.INTEGER;
		case LOGIC:
			return ColumnType.LOGIC;
		case TEXT:
			return ColumnType.TEXT;
		case TIME:
			return ColumnType.TIME;
		case MANY_TO_MANY:
			
		case MANY_TO_ONE:
		case MEMO:
		case ONE_TO_MANY:
		case ONE_TO_ONE:
		case UNKONW:
			
			default:
				return null;
		}
	}

	/**
	 * @param field
	 * @param queue 
	 * @return
	 */
	private TypeMapper readFieldTypeMapper(PropertyAccessor pa, EntityFieldModel field, Map<String, EntityModel> queue) {


		Type tm = pa.getAnnotation(Type.class);

		if (tm == null){

			if (domainModel.containsModelFor(pa.getValueType().getName())){
				// it is an entity. return it typemapper
				
				 EntityModelDataSetMapping map = this.mappings.get(pa.getValueType().getName());
				 
				 if (map == null){
					 // recursive call
					return processType(queue, pa.getValueType().getName());
				 } 
				 
				 return map.getTypeMapper();
				
			} else {
				if (pa.getValueType().isEnum()) {
					if (StorableEnum.class.isAssignableFrom(pa.getValueType())){
						return new EnumTypeMapper((Class<? extends StorableEnum>) pa.getValueType());
					} else {
						throw new IllegalStateException("Cannot persist " + pa.getValueType().getName() + " as it is not a StorableEnum" );
					}

				} else {
					
					String typeName;
					
					final ClassIntrospector<?> introspector = Introspector.of(pa.getValueType());
					if (introspector.isPrimitive()){
						typeName = introspector.getPrimitiveWrapper().getName();
					} else {
						typeName = pa.getValueType().getName();
					}

					// search default
					TypeMapper m = types.get(typeName);

					if (m == null){
						
						if (field.isIdentity()) {
							
							Id id = pa.getAnnotation(Id.class);
							
							return new IdentityTypeMapper(id.type());
						}
						throw new IllegalStateException("No TypeMapping found for class " + pa.getValueType().getName()); 
					}

					return m;
				}

			}
			

		} else {
			return Introspector.of(tm.type()).newInstance();
		}
	}



	/**
	 * @param mapping
	 * @param model 
	 * @param class1
	 */
	private void readDataSetName(EditableEntityModelDataSetMapping mapping, EntityModel model) {

		Dataset ds = model.getEntityClass().getAnnotation(Dataset.class);

		if (ds == null || ds.name().length() == 0){
			mapping.setDataSetName(resolveDataSetNameFromEntity(model));
			mapping.setInherintance(DatasetInheritance.NO_INHERITANCE);
		} else {
			mapping.setDataSetName(ds.name());
			mapping.setInherintance(ds.inherintance());
		}

		

	}



	protected String resolveDataSetNameFromEntity(EntityModel model){
		return model.getEntityName().toLowerCase();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerTypeMapper(TypeMapper typeMapper) {
		this.types.put(typeMapper.getMappedClassName(), typeMapper);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregisterTypeMapper(TypeMapper typeMapper) {
		this.types.remove(typeMapper.getMappedClassName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityFieldTypeMapper getEntityFieldTypeMapper(QualifiedName fieldName) {
		return this.mappings.get(fieldName.getQualifier()).getInstanceTypeMapper(fieldName.getQualifier()).getEntityFieldTypeMapper(fieldName);
	}






}
