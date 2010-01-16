package org.middleheaven.domain;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.domain.repository.Repository;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.identity.Identity;

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
		
		for (Class<?> type : classes){
			modelReader.read(type, builder);
		}
		
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
				}
				return fm;
			}

			
			@Override
			public EntityModelBuilder<E> setRepository(Repository<? extends E> repository) {
				@SuppressWarnings("unchecked") Class<E> type = (Class<E>) model.getEntityClass();
				domainModel.addEntityRepository(type, repository);
				return this;
			}

			@Override
			public EntityModelBuilder<E> setIdentityType(Class<? extends Identity> type) {
				model.setIdentityType(type);
				return this;
			}

			@Override
			public EnhancedCollection<FieldModelBuilder> fields() {
				return CollectionUtils.enhance(fields.values());
			}
			
		}
		
	}
	
	public static class SimpleFieldModelBuilder implements FieldModelBuilder {

		EditableEntityFieldModel fm;
		
		public SimpleFieldModelBuilder(EditableEntityFieldModel fm) {
			this.fm = fm;
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
		
	}
}
