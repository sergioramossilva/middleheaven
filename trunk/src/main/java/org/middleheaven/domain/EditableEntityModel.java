package org.middleheaven.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.domain.store.QualifiedName;
import org.middleheaven.logging.Log;
import org.middleheaven.util.identity.IntegerIdentity;

/**
 * Editable implementation of EntityModel
 */
public final class EditableEntityModel implements EntityModel {

	private Class<?> type;
	private Map<String, EntityFieldModel> fields = new HashMap<String, EntityFieldModel>();
	private EntityFieldModel identityFieldModel;
	private Class<?> identityType;
	
	/**
	 * 
	 * @param type the entity class.
	 */
	public EditableEntityModel(Class<?> type) {
		this.type = type;
	}

	@Override
	public EntityFieldModel identityFieldModel() {
		if (this.identityFieldModel == null){
			
			for (EntityFieldModel fieldModel : this.fields.values()){
				if(fieldModel.isIdentity()){
					this.identityFieldModel = fieldModel;
					break;
				}
			}
			
			if (this.identityFieldModel == null){
				Log.onBookFor(this.getClass()).warn("{0} has no identity field defined.",this.type);
				EditableEntityFieldModel eidentityFieldModel = new EditableEntityFieldModel(this.getEntityName(), "identity");
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
	public void addField(EntityFieldModel fieldModel) {
		if(fieldModel.isIdentity()){
			this.identityFieldModel = fieldModel;
		}
		this.fields.put(fieldModel.getLogicName().getName(), fieldModel);
	}
	
	@Override
	public EntityFieldModel fieldModel(QualifiedName logicName) {
		return this.fields.get(logicName.getName());
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

	@Override
	public String toString() {
		return "EditableEntityModel [type=" + this.type + "]";
	}

	@Override
	public Iterator<EntityFieldModel> iterator() {
		return this.fields.values().iterator();
	}


}
