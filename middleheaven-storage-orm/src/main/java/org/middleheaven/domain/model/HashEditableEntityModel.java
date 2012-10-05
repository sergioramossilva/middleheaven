package org.middleheaven.domain.model;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.metaclass.MetaClass;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;

/**
 * Editable implementation of EntityModel
 */
public final class HashEditableEntityModel implements EditableDomainEntityModel {

	private MetaClass type;
	private Map<String, EditableEntityFieldModel> fields = new HashMap<String, EditableEntityFieldModel>();
	private EditableEntityFieldModel identityFieldModel;
	private MetaClass identityType;
	private boolean assigned = false;
	
	/**
	 * 
	 * @param type the entity class.
	 */
	public HashEditableEntityModel(MetaClass type) {
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
			
//			if (this.identityFieldModel == null){
//				Log.onBookFor(this.getClass()).warn("{0} has no identity field defined.",this.type.getName());
//				BeanEditableEntityFieldModel eidentityFieldModel = new BeanEditableEntityFieldModel(this.getEntityName(), "identity");
//				eidentityFieldModel.setIsIdentity(true);
//				eidentityFieldModel.setDataType(DataType.INTEGER);
//				eidentityFieldModel.setUnique(true);
//				eidentityFieldModel.setValueType(IntegerIdentity.class);
//				
//				addField(eidentityFieldModel);
//				
//				identityFieldModel = eidentityFieldModel;
//			}
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
		this.fields.put(fieldModel.getName().getDesignation(), fieldModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsField(QualifiedName fieldQualifiedName) {
		return this.fields.containsKey(fieldQualifiedName.getDesignation());
	}

	
	@Override
	public EditableEntityFieldModel fieldModel(QualifiedName logicName) {
		EditableEntityFieldModel model = this.fields.get(logicName.getDesignation());
		if (model == null){
			throw new ModelingException("Field " + logicName + " does not exist in model");
		}
		return model;
	}

	@Override
	public MetaClass getEntityClass() {
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
	public void setIdentityType(MetaClass identityType) {
		this.identityType = identityType;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MetaClass getIdentityType(){
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


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIdentityAssigned() {
		return assigned;
	}
	
	public void setIdentityAssigned(boolean assigned) {
		this.assigned = assigned;
	}




}
