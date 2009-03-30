package org.middleheaven.storage;

import org.middleheaven.domain.DataType;

public class DefaultStorableFieldModel implements StorableFieldModel {

	private QualifiedName hardname;
	private DataType type;
	private boolean isTransient;
	private boolean isVersion;
	private boolean isKey;
	private Class<?> valueType;
	
	public DefaultStorableFieldModel(QualifiedName hardname, DataType type, Class<?> valueType) {
		super();
		this.hardname = hardname;
		this.type = type;
		this.valueType = valueType;
	}

	@Override
	public DataType getDataType() {
		return type;
	}
	
	@Override
	public QualifiedName getHardName() {
		return hardname;
	}
	
	public DefaultStorableFieldModel setTransient(boolean isTransient) {
		this.isTransient = isTransient;
		return this;
	}

	public DefaultStorableFieldModel setVersion(boolean isVersion) {
		this.isVersion = isVersion;
		return this;
	}

	public DefaultStorableFieldModel setKey(boolean isKey) {
		this.isKey = isKey;
		return this;
	}

	@Override
	public String getParam(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isIdentity(){
		return isKey;
	}
	
	@Override
	public boolean isTransient() {
		return isTransient;
	}
	@Override
	public boolean isVersion() {
		return isVersion;
	}

	@Override
	public Class<?> getValueClass() {
		return this.valueType;
	}

	@Override
	public QualifiedName getLogicName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorableEntityModel getEntityModel() {
		// TODO implement DefaultStorableFieldModel.getEntityModel
		return null;
	}

	@Override
	public boolean isUnique() {
		// TODO implement DefaultStorableFieldModel.isUnique
		return false;
	}

}
