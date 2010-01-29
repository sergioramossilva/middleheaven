package org.middleheaven.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.logging.Logging;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;

public final class EditableEntityModel implements EntityModel {

	private Class<?> type;
	private Map<String, EntityFieldModel> fields = new HashMap<String, EntityFieldModel>();
	private EntityFieldModel identityFieldModel;
	private Class<? extends Identity> identityType;
	
	public EditableEntityModel(Class<?> type) {
		this.type = type;
	}

	@Override
	public EntityFieldModel identityFieldModel() {
		if (identityFieldModel == null){
			for (EntityFieldModel fieldModel : this.fields.values()){
				if(fieldModel.isIdentity()){
					this.identityFieldModel = fieldModel;
					break;
				}
			}
			Logging.warn(this.type + " had no identity field defined");
			EditableEntityFieldModel eidentityFieldModel = new EditableEntityFieldModel(this.getEntityName(), "identity");
			eidentityFieldModel.setIsIdentity(true);
			eidentityFieldModel.setDataType(DataType.UNKWON);
			eidentityFieldModel.setUnique(true);
			eidentityFieldModel.setValueType(IntegerIdentity.class);
			
			addField(eidentityFieldModel);
			
			identityFieldModel = eidentityFieldModel;
		}
		return identityFieldModel;
	}

	public void addField(EntityFieldModel fieldModel) {
		if(fieldModel.isIdentity()){
			this.identityFieldModel = fieldModel;
		}
		fields.put(fieldModel.getLogicName().getName(), fieldModel);
	}
	
	@Override
	public EntityFieldModel fieldModel(QualifiedName logicName) {
		return fields.get(logicName.getName());
	}

	@Override
	public Collection<? extends EntityFieldModel> fields() {
		return Collections.unmodifiableCollection(fields.values());
	}

	@Override
	public Class<?> getEntityClass() {
		return type;
	}

	@Override
	public String getEntityName() {
		return type.getSimpleName();
	}

	@Override
	public Object newInstance() {
		return Introspector.of(type).newInstance();
	}

	public void setIdentityType(Class<? extends Identity> type) {
		this.identityType = type;
		
	}
	
	public Class<? extends Identity> getIdentityType(){
		return this.identityType;
	}

	@Override
	public String toString() {
		return "EditableEntityModel [type=" + type + "]";
	}


}
