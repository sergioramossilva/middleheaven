package org.middleheaven.storage;

import java.util.Collection;
import java.util.Collections;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyBagProxyHandler;
import org.middleheaven.domain.DataType;
import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.TransformCollection;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.validation.Consistencies;

public class DecoratorStorableEntityModel implements StorableEntityModel {

	
	private final EntityModel model;

	public DecoratorStorableEntityModel(EntityModel model){
		this.model = model;
	}
	
	
	public String toString(){
		return model.toString();
	}
	
	private StorableFieldModel convertField(final EntityFieldModel fModel) {
		Consistencies.consistNotNull(fModel);
		
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

			@Override
			public Class<?> getAggregationClass() {
				return fModel.getAggregationClass();
			}
			
			StorableDataTypeModel model;
			@Override
			public StorableDataTypeModel getDataTypeModel() {
				if (model == null){
					Class<? extends StorableDataTypeModel> type =  fModel.getDataType().isReference() ? ReferenceStorableDataTypeModel.class :  StorableDataTypeModel.class; 
					model = (StorableDataTypeModel) Introspector.of(fModel.getDataTypeModel()).newProxyInstance(
								new PropertyBagProxyHandler(),
								type
								
					);

				}
				return	model;
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

	@Override
	public StorableFieldModel fieldReferenceTo(Class<?> type) {
		for (EntityFieldModel tfm : this.model.fields()){
			StorableFieldModel sf = this.convertField(tfm);
			
			if (sf.getDataType().isToOneReference() && sf.getValueClass().equals(type)){
				return sf;
			}
		}
		return null;
	}

}
