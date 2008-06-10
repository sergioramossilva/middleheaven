package org.middleheaven.storage;


public interface StorableEntityModel{

	public <T> T instanceFor(Class<T> type, Storable s);

	public String hardNameForEntity();
	
	public StorableFieldModel fieldModel(QualifiedName logicName);
	public StorableFieldModel keyFieldModel();
}
