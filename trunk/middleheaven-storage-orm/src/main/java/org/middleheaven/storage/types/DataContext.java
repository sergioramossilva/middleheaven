package org.middleheaven.storage.types;

public interface DataContext {

	void put(String nameFor, Object propertyValue);

	Object get(String string);

}
