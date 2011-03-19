package org.middleheaven.domain.model;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.logging.Log;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.identity.IntegerIdentity;

/**
 * Editable implementation of EntityModel
 */
public final class HashEditableEntityModel implements EditableDomainEntityModel {

	private Class<?> type;
	private Map<String, EditableEntityFieldModel> fields = new HashMap<String, EditableEntityFieldModel>();
	private EditableEntityFieldModel identityFieldModel;
	private Class<?> identityType;
	
	/**
	 * 
	 * @param type the entity class.
	 */
	public HashEditableEntityModel(Class<?> type) {
		this.type = type;
	}

	@Override
	public EditableEntityFieldModel identityFieldModel() {
		if (this.identityFieldModel == null){
			
			for (EditableEntityFieldModel fieldModel : this.fields.values()){
				if(fieldModel.isIdentity()){
					this.identityFieldModel = fieldModel;
					break;
				}
			}
			
			if (this.identityFieldModel == null){
				Log.onBookFor(this.getClass()).warn("{0} has no identity field defined.",this.type);
				BeanEditableEntityFieldModel eidentityFieldModel = new BeanEditableEntityFieldModel(this.getEntityName(), "identity");
				eidentityFieldModel.setIsIdentity(true);
				eidentityFieldModel.setDataType(DataType.INTEGER);
				eidentityFieldModel.setUnique(true);
				eidentityFieldModel.setValueType(IntegerIdentity.class);
				
				addField(eidentityFieldModel);
				
				identityFieldModel = eidentityFieldModel;
			}
		}
		return identityFieldModel;
	}

	/**
	 * 
	 * @param fieldModel {@code EntityFieldModel} to add
	 */
	public void addField(EditableEntityFieldModel fieldModel) {
		if (fieldModel.isIdentity()) {
			this.identityFieldModel = fieldModel;
		}
		this.fields.put(fieldModel.getName().getName(), fieldModel);
	}

	@Override
	public EditableEntityFieldModel fieldModel(QualifiedName logicName) {
		EditableEntityFieldModel model = this.fields.get(logicName.getName());
		if (model == null){
			throw new ModelingException("Field " + logicName + " does not exist in model");
		}
		return model;
	}

	@Override
	public Class<?> getEntityClass() {
		return this.type;
	}

	@Override
	public String getEntityName() {
		return this.type.getSimpleName();
	}

	/**
	 * 
	 * @param identityType the identity's type
	 */
	public void setIdentityType(Class<?> identityType) {
		this.identityType = identityType;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Class<?> getIdentityType(){
		return this.identityType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "EditableEntityModel [type=" + this.type + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<? extends EditableEntityFieldModel> fields() {
		return CollectionUtils.enhance(this.fields.values());
	}



}
