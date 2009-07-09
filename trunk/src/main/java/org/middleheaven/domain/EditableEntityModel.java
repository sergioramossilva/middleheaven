package org.middleheaven.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.util.identity.Identity;

public final class EditableEntityModel implements EntityModel {

	private Class<?> type;
	private Map<String, EntityFieldModel> fields = new HashMap<String, EntityFieldModel>();
	private EntityFieldModel keyModel;
	private Class<? extends Identity> identityType;
	
	public EditableEntityModel(Class<?> type) {
		this.type = type;
	}

	@Override
	public EntityFieldModel identityFieldModel() {
		return keyModel;
	}

	public void addField(EntityFieldModel fieldModel) {
		if(fieldModel.isIdentity()){
			this.keyModel = fieldModel;
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


}
