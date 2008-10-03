package org.middleheaven.web;


public abstract class AbstractContext implements Context {

	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		ContextScope[] scopes = new ContextScope[]{
			ContextScope.PARAMETERS,
			ContextScope.REQUEST,
			ContextScope.SESSION,
			ContextScope.APPLICATION,
			ContextScope.CONFIGURATION};
		
		for (ContextScope scope : scopes){
			T obj = this.getAttribute(scope, name, type);
			if (obj!=null){
				return obj;
			}
		}
		return null;
	}
}
