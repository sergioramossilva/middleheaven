package org.middleheaven.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.middleheaven.core.reflection.ClassNotFoundReflectionException;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.domain.annotations.Column;
import org.middleheaven.domain.annotations.Entity;
import org.middleheaven.domain.annotations.Key;
import org.middleheaven.domain.annotations.ManyToMany;
import org.middleheaven.domain.annotations.ManyToOne;
import org.middleheaven.domain.annotations.OneToMany;
import org.middleheaven.domain.annotations.OneToOne;
import org.middleheaven.domain.annotations.Temporal;
import org.middleheaven.domain.annotations.Transient;
import org.middleheaven.domain.annotations.Unique;
import org.middleheaven.domain.annotations.ValueObject;
import org.middleheaven.domain.annotations.Version;
import org.middleheaven.domain.repository.Repository;
import org.middleheaven.domain.repository.RepositoryRegister;
import org.middleheaven.domain.repository.StandardEntityRepository;
import org.middleheaven.logging.Logging;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.DataStorageService;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.util.identity.Identity;

/**
 * <code>DomainModel</code> builded by reading annotations in the entity classes.
 */
public class AnnotatedDomainModel implements DomainModel{

	private RepositoryRegister repositoryRegistry = new RepositoryRegister();
	private Map<String, StorableEntityModel> storableEntityModels = new TreeMap<String, StorableEntityModel> ();
	private Map<String, Class> identitities= new TreeMap<String, Class>();

	private HardnameMapper hardnameMapper;

	public static  AnnotatedDomainModel model (){
		return new AnnotatedDomainModel(new AnnotatedHardnameMapper());
	}

	public static  AnnotatedDomainModel model (Package entitiesPackage){
		AnnotatedDomainModel model = new AnnotatedDomainModel(new AnnotatedHardnameMapper());
		model.addAllEntities(entitiesPackage);
		return model;
	}

	public static  AnnotatedDomainModel model (Package entitiesPackage, HardnameMapper hardnameMapper){
		AnnotatedDomainModel model = new AnnotatedDomainModel(hardnameMapper);
		model.addAllEntities(entitiesPackage);
		return model;
	}

	private AnnotatedDomainModel(HardnameMapper hardnameMapper){
		Domain.model = this;
		this.hardnameMapper = hardnameMapper;
	}

	@Override
	public <E> Repository<E> repositoryOf(Class<E> entityType) {
		Repository<E> rep = repositoryRegistry.of(entityType);
		rep.setDomainModel(this);
		return rep;
	}


	@Override
	public <E, R extends Repository<E>> R repository(Class<R> repositoryType) {

		R rep = repositoryRegistry.getRepository(repositoryType);
		rep.setDomainModel(this);
		return rep;
	}

	@Override
	public <E> DataStorage storageOf(Class<E> entityType) {
		DataStorageService service = ServiceRegistry.getService(DataStorageService.class);
		return service.getStorage();
	}

	@Override
	public <E> void addEntity(Class<E> entityType,Repository<? extends E> repository) {
		this.repositoryRegistry.setRepository(entityType, repository);
		// add annotaded storemodel

		this.addStorableModel(entityType);

	}

	private void addStorableModel(Class<?> entityType){
		this.storableEntityModels.put(entityType.getName(), new AnnotatedStorableEntityModel(entityType));
	}

	public <E> void addEntity(Class<E> entityType) {

		// look for a class with name [EntityType]Repository in the same package
		Package pack = entityType.getPackage();
		String[] searchPaths = new String[]{pack.getName() , pack.getName() + ".repository" , pack.getName() + ".repositories"};


		for (int i=0;i < searchPaths.length;i++){
			String path =  searchPaths[i] + "." +  entityType.getSimpleName() + "Repository";
			try{
				Repository<E> rep = ReflectionUtils.newInstance(path, Repository.class);
				this.addEntity(entityType, rep);
				return;
			} catch (ClassCastException e){
				throw new ClassCastException(path + " is not a Repository");
			} catch (ClassNotFoundReflectionException e){
				// not found. Try another location
			}
		}

		// not found. 
		// use default repository
		this.addEntity(entityType, new StandardEntityRepository(entityType));
	}

	public void addAllEntities(){
		addAllEntities(this.getClass().getPackage());
	}

	public void addAllEntities(Package entitiesPackage){

		Set<Class<?>> entities = ReflectionUtils.getPackageClasses(entitiesPackage);

		for (Class<?> c : entities){
			if (c.isAnnotationPresent(Entity.class)){
				addEntity(c);
			}
		}

	}


	private class AnnotatedStorableEntityModel implements StorableEntityModel{

		private Class entityType;
		private String logicEntityName;
		private Map<QualifiedName ,StorableFieldModel > fields = new HashMap<QualifiedName ,StorableFieldModel >();
		private StorableFieldModel keyFieldModel;
		private String hardEntityName;

		public String toString(){
			return "StorableModel of " + logicEntityName;
		}


		public AnnotatedStorableEntityModel(Class entityType){
			this.entityType = entityType;

			logicEntityName = entityType.getSimpleName().toLowerCase();
			hardEntityName = hardnameMapper.getEntityHardname(entityType);

			Iterable<PropertyAccessor> propertyAccessors = ReflectionUtils.getPropertyAccessors(entityType);

			AnnotatedStorableFieldModel possibleKey=null;

			if (!propertyAccessors.iterator().hasNext()){
				throw new IllegalModelException("No public fields found for entity " + logicEntityName + ".");
			} else {
				for (PropertyAccessor pa : propertyAccessors){
					AnnotatedStorableFieldModel sfm = new AnnotatedStorableFieldModel(
							pa, 
							QualifiedName.qualify(logicEntityName, pa.getName().toLowerCase())
					);

					if (sfm.isKey()){
						keyFieldModel = sfm;
					}else if (sfm.getDataType().equals(DataType.IDENTITY)){
						possibleKey = sfm;
					}

					this.fields.put(sfm.getLogicName() , sfm);
				}

				if (keyFieldModel==null){
					// search for one returning Identity
					if (possibleKey!=null){
						// possibly the key field.
						Logging.warn("No key field found for entity " + logicEntityName + " but found possible match in " + possibleKey.getLogicName());
						keyFieldModel = possibleKey;

					} else {
						throw new IllegalModelException("No key field found for entity " + logicEntityName + ". Annotate the key acessor with @Key");
					}
				}
			}

		}


		@Override
		public StorableFieldModel fieldModel(QualifiedName logicName) {
			return fields.get(logicName);
		}

		@Override
		public Collection<StorableFieldModel> fields() {
			return Collections.unmodifiableCollection(fields.values());
		}

		@Override
		public String getEntityLogicName() {
			return logicEntityName;
		}

		@Override
		public Collection<StorableFieldModel> uniqueFields() {
			Collection<StorableFieldModel> uniques = new LinkedList<StorableFieldModel>();
			for (StorableFieldModel f :fields.values()) {
				if (f.isUnique()){
					uniques.add(f);
				}
			}
			return null;
		}


		@Override
		public String getEntityHardName() {
			return hardEntityName;
		}


		@Override
		public Object newInstance() {
			return ReflectionUtils.newInstance(entityType);
		}


		@Override
		public Class<?> getEntityClass() {
			return entityType;
		}


		@Override
		public String getEntityName() {
			return logicEntityName;
		}


		@Override
		public StorableFieldModel identityFieldModel() {
			return keyFieldModel;
		}


		private class AnnotatedStorableFieldModel implements StorableFieldModel{

			DataType dataType;
			QualifiedName logicName;
			boolean isKey;
			boolean isTransient;
			boolean isVersion;
			boolean isUnique;
			private QualifiedName hardName;
			private Map<String, String> params = new TreeMap<String,String>();
			private Class valueType;


			public AnnotatedStorableFieldModel(PropertyAccessor pa , QualifiedName logicName) {
				this.logicName = logicName;
				this.isTransient = pa.isAnnotatedWith(Transient.class);
				this.isVersion = pa.isAnnotatedWith(Version.class);
				this.isUnique = pa.isAnnotatedWith(Unique.class);
				this.valueType = pa.getValueType();

				if(pa.isAnnotatedWith(Key.class)){
					this.isKey = true;
					Key key = pa.getAnnotation(Key.class);
					Class<?> identityType= key.type();
					identitities.put(pa.getParentClass().getName(), identityType);

				}

				if (pa.isAnnotatedWith(ManyToOne.class)){
					this.dataType = DataType.MANY_TO_ONE;
					ManyToOne ref = pa.getAnnotation(ManyToOne.class);
					String fieldName = ref.targetIdentityField();
					if (fieldName.isEmpty()){
						fieldName = logicName.getColumnName();
					}
					params.put("targetField", fieldName);
					params.put("targetFieldHardName", hardnameMapper.getFieldHardname(pa.getParentClass(), fieldName));
				} else if (pa.isAnnotatedWith(OneToOne.class)){
					this.dataType = DataType.ONE_TO_ONE;
					OneToOne ref = pa.getAnnotation(OneToOne.class);
					String fieldName = ref.targetIdentityField();
					if (fieldName.isEmpty()){
						fieldName = logicName.getColumnName();
					}
					params.put("targetField", fieldName);
					params.put("targetFieldHardName", hardnameMapper.getFieldHardname(pa.getParentClass(), fieldName));
				} else if (pa.isAnnotatedWith(OneToMany.class)){
					this.dataType = DataType.ONE_TO_MANY;
					OneToMany ref = pa.getAnnotation(OneToMany.class);
					Class target = ref.target();

				} else if (pa.isAnnotatedWith(ManyToMany.class)){
					this.dataType = DataType.MANY_TO_MANY;
					ManyToMany ref = pa.getAnnotation(ManyToMany.class);
					Class target = ref.target();

				}

				if(pa.isAnnotatedWith(Column.class)){
					this.isKey = true;
					Column column = pa.getAnnotation(Column.class);
					params.put("size",Integer.toString(column.length()));
				} else {
					params.put("size","100");
				}


				hardName = QualifiedName.qualify(
						AnnotatedStorableEntityModel.this.hardEntityName, 
						hardnameMapper.getFieldHardname(pa.getParentClass(), logicName.getColumnName())
				);

				Class<?> valueType = pa.getValueType();

				if ( this.dataType == null){
					if (matchTypes(valueType, String.class) ){
						this.dataType = DataType.TEXT;
					} else if (matchTypes(valueType,Integer.class ,int.class, Byte.class, byte.class, Short.class, short.class)  ){
						this.dataType = DataType.INTEGER;
					} else if (matchTypes(valueType,Identity.class)  ){
						this.dataType = DataType.IDENTITY;
					} else if (matchTypes(valueType,CalendarDate.class)){
						this.dataType = DataType.DATE;
					} else if (matchTypes(valueType,CalendarDateTime.class)){
						this.dataType = DataType.DATETIME;
						if (pa.isAnnotatedWith(Temporal.class)){
							this.dataType = pa.getAnnotation(Temporal.class).value();
						} 
					} else if (matchTypes(valueType, Date.class,Calendar.class)){
						if (pa.isAnnotatedWith(Temporal.class)){
							this.dataType = pa.getAnnotation(Temporal.class).value();
						} else {
							throw new IllegalModelException(logicName.toString() + " must be annotated with @Temporal");
						}
					} else if (matchTypes(valueType,Boolean.class,boolean.class)){
						this.dataType = DataType.LOGIC;
					} else if ( matchTypes(valueType,Double.class , double.class , Float.class , float.class, BigDecimal.class)){
						this.dataType = DataType.DECIMAL;
					}
				}
			}

			private boolean matchTypes(Class<?> candidate , Class<?> ... types){
				for (Class<?> t : types){
					if (candidate.isAssignableFrom(t)){
						return true;
					}
				}
				return false;
			}

			@Override
			public StorableEntityModel getEntityModel() {
				return AnnotatedStorableEntityModel.this;
			}

			@Override
			public DataType getDataType() {
				return dataType;
			}

			@Override
			public QualifiedName getHardName() {
				return hardName;
			}

			public QualifiedName getLogicName() {
				return logicName;
			}

			@Override
			public String getParam(String name) {
				return params.get(name);
			}

			@Override
			public boolean isKey() {
				return isKey;
			}

			@Override
			public boolean isTransient() {
				return this.isTransient;
			}

			@Override
			public boolean isVersion() {
				return isVersion;
			}

			@Override
			public boolean isUnique() {
				return isUnique;
			}

			@Override
			public Class<?> getValueClass() {
				return valueType;
			}

		}




	}


	@Override
	public <I extends Identity> Class<I> indentityTypeFor(Class<?> entityType) {
		return identitities.get(entityType.getName());
	}

	@Override
	public <T extends EntityModel> Collection<T> entitiesModels() {
		return (Collection<T>) Collections.unmodifiableCollection(this.storableEntityModels.values());
	}

	@Override
	public EntityModel getEntityModelFor(Class<?> entityType) {
		return storableEntityModels.get(entityType.getName());
	}



}
