package org.middleheaven.storage.assembly;

public interface DataContext {

	void put(String nameFor, Object propertyValue);

	Object get(String string);

}
