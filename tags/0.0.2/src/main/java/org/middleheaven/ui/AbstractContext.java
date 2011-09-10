package org.middleheaven.ui;


public abstract class AbstractContext implements AttributeContext {

	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		ContextScope[] scopes = ContextScope.values(); 
		
		for (ContextScope scope : scopes){
			T obj = this.getAttribute(scope, name, type);
			if (obj!=null){
				return obj;
			}
		}
		return null;
	}
}
