package org.middleheaven.ui;

import java.util.Enumeration;


public interface AttributeContext {

	public final String APPLICATION_CONTEXT_FILE_REPOSITORY = "_appContextFR";
	public final String UPLOADS_FILE_REPOSITORY = "_uploadsFR";
	public final String APPLICATION =  "_webApplication";
	public final String LOCALE =  "_locale";
	
	public <T> T getAttribute(ContextScope scope , String name, Class<T> type);
	public void setAttribute(ContextScope scope , String name, Object value);
	public <T> T getAttribute(String name, Class<T> type);
	public Enumeration<String> getAttributeNames(ContextScope scope);
	
	
	
	
}
