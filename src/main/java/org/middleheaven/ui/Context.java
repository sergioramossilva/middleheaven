package org.middleheaven.ui;

import java.util.Enumeration;

public interface Context {

	
	public <T> T getAttribute(ContextScope scope , String name, Class<T> type);
	public void setAttribute(ContextScope scope , String name, Object value);
	public <T> T getAttribute(String name, Class<T> type);
	public Enumeration<String> getAttributeNames(ContextScope scope);
}
