package org.middleheaven.process;




/**
 * An attribute map-like context that organizes attributes names and values into scopes.
 * 
 * An {@link AttributeContext} implementations 
 */
public interface AttributeContext {

	public final String APPLICATION_CONTEXT_FILE_REPOSITORY = "_appContextFR";
	public final String UPLOADS_FILE_REPOSITORY = "_uploadsFR";
	public final String APPLICATION =  "_webApplication";
	public final String LOCALE =  "_locale";

	/**
	 * Gets the attribute.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param scope
	 *            the scope
	 * @param name
	 *            the name
	 * @param type
	 *            the type
	 * @return the attribute
	 */
	public <T> T getAttribute(ContextScope scope , String name, Class<T> type);
	
	/**
	 * Sets the attribute.
	 * 
	 * @param scope
	 *            the scope
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public void setAttribute(ContextScope scope , String name, Object value);
	
	
	/**
	 * Remove atribute with the given name from the scope.
	 * @param scope
	 * @param name
	 */
	public void removeAttribute(ContextScope scope, String name);
	
	/**
	 * Searches all the {@link ContextScope} for the wanted attribute.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param name
	 *            the name
	 * @param type
	 *            the type
	 * @return the attribute
	 */
	public <T> T getAttribute(String name, Class<T> type);
	
	
	/**
	 * 
	 * @param scope the scope we wish to 
	 * @return the number of attributes that exist in the scope; 
	 */
	public ScopedAttributesResolutionStrategy getScopeAttributeContext(ContextScope scope);
	
	
}
