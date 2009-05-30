package org.middleheaven.storage;

import java.util.Collection;
import java.util.Collections;

import org.middleheaven.domain.DataType;
import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.TransformCollection;
import org.middleheaven.util.identity.Identity;

public class DecoratorStorableEntityModel implements StorableEntityModel {

	
	private final  EntityModel model;

	public DecoratorStorableEntityModel(EntityModel model){
		this.model = model;
	}
	
	private StorableFieldModel convertField(final EntityFieldModel fModel) {
		return new StorableFieldModel(){

			@Override
			public StorableEntityModel getEntityModel() {
				return DecoratorStorableEntityModel.this;
			}

			@Override
			public QualifiedName getHardName() {
				return this.getLogicName();
			}

			@Override
			public String getParam(String key) {
				return fModel.getParam(key);
			}

			@Override
			public DataType getDataType() {
				return fModel.getDataType();
			}

			@Override
			public QualifiedName getLogicName() {
				return fModel.getLogicName();
			}

			@Override
			public Class<?> getValueClass() {
				return fModel.getValueClass();
			}

			@Override
			public boolean isIdentity() {
				return fModel.isIdentity();
			}

			@Override
			public boolean isTransient() {
				return fModel.isTransient();
			}

			@Override
			public boolean isUnique() {
				return fModel.isUnique();
			}

			@Override
			public boolean isVersion() {
				return fModel.isVersion();
			}
			
		};
	}
	
	@Override
	public StorableFieldModel fieldModel(QualifiedName logicName) {
		return convertField(model.fieldModel(logicName));
	}


	@Override
	public  Collection<StorableFieldModel> fields() {
		@SuppressWarnings("unchecked")  Collection<EntityFieldModel> all = (Collection<EntityFieldModel>) model.fields();
		return new TransformCollection<EntityFieldModel,StorableFieldModel>(all, new Classifier<StorableFieldModel,EntityFieldModel>(){

			@Override
			public StorableFieldModel classify(EntityFieldModel object) {
				return convertField(object);
			}
			
		});
	}

	@Override
	public Class<?> getEntityClass() {
		return model.getEntityClass();
	}

	@Override
	public String getEntityHardName() {
		return model.getEntityName();
	}

	@Override
	public String getEntityLogicName() {
		return model.getEntityName();
	}

	@Override
	public StorableFieldModel identityFieldModel() {
		return convertField(model.identityFieldModel());
	}



	@Override
	public Object newInstance() {
		return model.newInstance();
	}

	@Override
	public Collection<StorableFieldModel> uniqueFields() {
		// TODO implement DecoratorStorableEntityModel.uniqueFields
		return Collections.emptySet();
	}

	@Override
	public String getEntityName() {
		return model.getEntityName();
	}

	@Override
	public Class<? extends Identity> getIdentityType() {
		return model.getIdentityType();
	}

}
