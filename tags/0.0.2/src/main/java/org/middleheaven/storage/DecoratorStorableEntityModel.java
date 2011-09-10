package org.middleheaven.storage;

import java.util.Collection;
import java.util.Collections;

import org.middleheaven.model.domain.DataType;
import org.middleheaven.model.domain.EntityFieldModel;
import org.middleheaven.model.domain.EntityModel;
import org.middleheaven.model.domain.ReferenceDataTypeModel;
import org.middleheaven.model.domain.TextDataTypeModel;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.collections.TransformedEnumerable;
import org.middleheaven.validation.Consistencies;

public class DecoratorStorableEntityModel implements StorableEntityModel {

	
	private final EntityModel model;

	public DecoratorStorableEntityModel(EntityModel model){
		this.model = model;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString(){
		return model.toString();
	}
	
	private StorableFieldModel convertField(final EntityFieldModel fModel) {
		Consistencies.consistNotNull(fModel);
		
		return new StorableFieldModel(){

			
			private StorableDataTypeModel model;
			
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
		
			@Override
			public StorableDataTypeModel getDataTypeModel() {
				if (model == null){
					if (fModel.getDataType().isReference()){
						
						model = new ReferenceStorableDataTypeModelAdapter((ReferenceDataTypeModel) fModel.getDataTypeModel());

					} else if (fModel.getDataType().isTextual()){
						model = new TextStorableDataTypeAdapter((TextDataTypeModel)fModel.getDataTypeModel());
						
					} else {
						
						model = new DefaultStorableDataTypeAdapter(fModel.getDataTypeModel());
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
		for (StorableFieldModel tfm : fields()){
			StorableFieldModel sf = this.convertField(tfm);
			
			if (sf.getDataType().isToOneReference() && sf.getValueType().equals(type)){
				return sf;
			}
		}
		return null;
	}



	@Override
	public Enumerable<? extends StorableFieldModel> fields() {
		return CollectionUtils.enhance(TransformedEnumerable.transform(model.fields(), new Classifier<StorableFieldModel, EntityFieldModel>(){

			@Override
			public StorableFieldModel classify(EntityFieldModel object) {
				return convertField(object);
			}
			
		}));
	}

}
