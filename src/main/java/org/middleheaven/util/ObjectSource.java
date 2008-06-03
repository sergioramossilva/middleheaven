package org.middleheaven.util;

public interface ObjectSource {

	
	public <T> T getObject(Class<T> type);
}
