package org.middleheaven.storage;

import java.util.Collection;


public interface StorableEntityModel{

	public <T> T instanceFor(Class<T> type);

	public String hardNameForEntity();
	public String logicNameForEntity();
	
	public StorableFieldModel fieldModel(QualifiedName logicName);
	public StorableFieldModel keyFieldModel();

	public Collection<StorableFieldModel> fields();

	
}
