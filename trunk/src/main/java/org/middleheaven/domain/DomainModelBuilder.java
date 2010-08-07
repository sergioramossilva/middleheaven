package org.middleheaven.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.middleheaven.core.dependency.DependencyResolver;
import org.middleheaven.core.dependency.InicializationNotPossibleException;
import org.middleheaven.core.dependency.InicializationNotResolvedException;
import org.middleheaven.core.dependency.Starter;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.domain.repository.Repository;
import org.middleheaven.logging.Log;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

public final class DomainModelBuilder {

	private final CompositeModelReader modelReader = new CompositeModelReader();
	
	public DomainModelBuilder(){
		modelReader.add(new DefaultAnnotatedModelReader());
	}
	
	public void addReader(ModelReader reader){
		modelReader.add(reader);
	}
	
	public void removeReader(ModelReader reader){
		modelReader.remove(reader);
	}
	
	public DomainModel build(DomainClasses classes){
		
		final SimpleModelBuilder builder = new SimpleModelBuilder();
		
		new DependencyResolver(Log.onBookFor(this.getClass())).resolve(classes.entities, new Starter<Class<?>>(){

			@Override
			public void inicialize(Class<?> type)
					throws InicializationNotResolvedException,
					InicializationNotPossibleException {
				
				modelReader.read(type, builder);
			}

			@Override
			public void inicializeWithProxy(Class<?> dependableProperties)
					throws InicializationNotResolvedException,
					InicializationNotPossibleException {
				
				throw new UnsupportedOperationException();
			}

			@Override
			public List<Class<?>> sort(final Collection<Class<?>> dependencies) {

				List<ClassDependency> deps = new ArrayList<ClassDependency>(dependencies.size());

				for (Class<?> c: dependencies){
					deps.add(new ClassDependency (c, dependencies));
				}
				
				Collections.sort(deps, new Comparator<ClassDependency>(){

					@Override
					public int compare(ClassDependency a,	ClassDependency b) {
						return a.getDepenciesCount() - b.getDepenciesCount();
					}
					
				});
				List<Class<?>> result = new ArrayList<Class<?>>(dependencies.size());
				for (ClassDependency cd : deps){
					result.add(cd.getType());
				}
				
				return result;
			}

			class ClassDependency {
				
				private Class<?> type;
				private Collection<Class<?>> depends;
				
				public ClassDependency(Class<?> type, final Collection<Class<?>> dependencies){
					this.type = type;
					
					this.depends = Introspector.of(type).inspect().properties().retriveAll().collect(new Classifier<Class<?>,PropertyAccessor>(){

						@Override
						public Class<?> classify(PropertyAccessor obj) {
							if( dependencies.contains(obj.getValueType())){
								return obj.getValueType();
							}
							return null;
						}

					});
						
					
				}
				
				public int getDepenciesCount() {
					return depends.size();
				}

				public Class getType(){
					return type;
				}
				
			}
			
		});
		
		return builder.getModel();
	}
	
	public static class SimpleModelBuilder implements ModelBuilder {

		final Map<String, EntityModelBuilder> entities = new HashMap<String, EntityModelBuilder>();
		EditableDomainModel domainModel = new EditableDomainModel();
		
		@Override
		public <E> EntityModelBuilder<E> getEntity(Class<E> type) {
			EntityModelBuilder<E> em = entities.get(type.getName());
			if (em == null){
				EditableEntityModel model = new EditableEntityModel(type);
				em = new SimpleEntityModelBuilder<E>(model);
				entities.put(type.getName(), em);
				domainModel.addEntityModel(type, model);
			}
			return em;
		}

		public DomainModel getModel() {
			return domainModel;
		}

		public class SimpleEntityModelBuilder<E> implements EntityModelBuilder<E> {

			private EditableEntityModel model;
			private Map<String, FieldModelBuilder> fields = new HashMap<String, FieldModelBuilder>();
			private FieldModelBuilder identityField;
			
			public SimpleEntityModelBuilder(EditableEntityModel model) {
				this.model = model;
			}

			@Override
			public FieldModelBuilder getField(String name) {
				FieldModelBuilder fm = fields.get(name);
				if (fm == null){
					EditableEntityFieldModel fem = new EditableEntityFieldModel(model.getEntityName(), name);
					model.addField(fem);
					fm = new SimpleFieldModelBuilder(fem);
					fields.put(name, fm);
					if (fm.isIdentity()){
						identityField = fm;
					}
				}
				return fm;
			}

			
			@Override
			public EntityModelBuilder<E> setRepository(Repository<? extends E> repository) {
				@SuppressWarnings("unchecked") Class<E> type = (Class<E>) model.getEntityClass();
				//domainModel.addEntityRepository(type, repository);
				return this;
			}

			@Override
			public EntityModelBuilder<E> setIdentityType(Class<?> type) {
				model.setIdentityType(type);
				return this;
			}

			@Override
			public EnhancedCollection<FieldModelBuilder> fields() {
				return CollectionUtils.enhance(fields.values());
			}

			@Override
			public FieldModelBuilder getIdentityField() {
				if(this.identityField==null){
					for (FieldModelBuilder fmb : this.fields.values()){
						if(fmb.isIdentity()){
							identityField = fmb;
							break;
						}
					}
				}
				return identityField;
			}

			@Override
			public Class<?> getIdentityType() {
				return model.getIdentityType();
			}
			
		}
		
	}
	
	public static class SimpleFieldModelBuilder implements FieldModelBuilder {

		EditableEntityFieldModel fm;
		
		public SimpleFieldModelBuilder(EditableEntityFieldModel fm) {
			this.fm = fm;
		}

		
		public String toString(){
			return fm.name.toString();
		}
		
		@Override
		public DataType getDataType() {
			return fm.getDataType();
		}

		@Override
		public String getName() {
			return fm.getLogicName().getName();
		}

		@Override
		public boolean isIdentity() {
			return fm.isIdentity();
		}

		@Override
		public FieldModelBuilder setDataType(DataType dataType) {
			fm.setDataType(dataType);
			return this;
		}

		@Override
		public FieldModelBuilder setIdentity(boolean b) {
			fm.setIsIdentity(b);
			return this;
		}


		@Override
		public FieldModelBuilder setTransient(boolean isTransient) {
			fm.setTransient(isTransient);
			return this;
		}

		@Override
		public FieldModelBuilder setUnique(boolean isUnique) {
			fm.setUnique(isUnique);
			return this;
		}


		@Override
		public FieldModelBuilder setVersion(boolean isVersion) {
			fm.setVersion(isVersion);
			return this;
		}


		@Override
		public FieldModelBuilder setDataTypeModel(DataTypeModel typeModel) {
			fm.setDataTypeModel(typeModel);
			return this;
		}

		@Override
		public FieldModelBuilder setValueType(Class<?> valueType) {
			fm.setValueType(valueType);
			return this;
		}

		@Override
		public FieldModelBuilder setNullable(boolean nullable) {
			fm.setNullable(nullable);
			return this;
		}

		@Override
		public Class<?> getValueType() {
			return fm.getValueType();
		}
		
	}
}
