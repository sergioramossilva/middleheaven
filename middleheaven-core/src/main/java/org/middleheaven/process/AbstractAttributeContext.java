package org.middleheaven.process;



/**
 * {@link AttributeContext} base implementation.
 */
public abstract class AbstractAttributeContext implements AttributeContext {

	private final static ContextScope[] scopes = new ContextScope[]{ContextScope.PARAMETERS, ContextScope.REQUEST, ContextScope.SESSION , ContextScope.APPLICATION};
	
	@Override
	public <T> T getAttribute(String name, Class<T> type) {

		for (ContextScope scope : scopes){
			T obj = this.getAttribute(scope, name, type);
			if (obj!=null){
				return obj;
			}
		}
		return null;
	}
	
	
	

}
