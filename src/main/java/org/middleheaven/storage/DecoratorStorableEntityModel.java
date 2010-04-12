package org.middleheaven.storage;

import java.util.Collection;
import java.util.Collections;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyBagProxyHandler;
import org.middleheaven.domain.DataType;
import org.middleheaven.domain.DataTypeModel;
import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.domain.ReferenceDataTypeModel;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.TransformedCollection;
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
			public Class<?> getValueType() {
				return fModel.getValueType();
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
			
			@Override
			public boolean isNullable() {
				return fModel.isNullable();
			}
			
			StorableDataTypeModel model;
			@Override
			public StorableDataTypeModel getDataTypeModel() {
				if (model == null){
					if (fModel.getDataType().isReference()){
						ReferenceStorableDataTypeModel rm = (ReferenceStorableDataTypeModel) Introspector.of(fModel.getDataTypeModel()).newProxyInstance(
								new PropertyBagProxyHandler(),
								ReferenceStorableDataTypeModel.class
						);
						
						ReferenceDataTypeModel dtm = (ReferenceDataTypeModel)fModel.getDataTypeModel();
						
						rm.setTargetFieldHardname(dtm.getTargetFieldName());
						rm.setTargetFieldname(dtm.getTargetFieldName());

						model = rm;
					} else {
						model = (StorableDataTypeModel) Introspector.of(fModel.getDataTypeModel()).newProxyInstance(
								new PropertyBagProxyHandler(),
								StorableDataTypeModel.class
						);
					}

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
		return TransformedCollection.transform(all, new Classifier<StorableFieldModel,EntityFieldModel>(){

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
	public Class<?> getIdentityType() {
		return model.getIdentityType();
	}

	@Override
	public StorableFieldModel fieldReferenceTo(Class<?> type) {
		for (EntityFieldModel tfm : this.model.fields()){
			StorableFieldModel sf = this.convertField(tfm);
			
			if (sf.getDataType().isToOneReference() && sf.getValueType().equals(type)){
				return sf;
			}
		}
		return null;
	}

}
