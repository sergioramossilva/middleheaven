/**
 * 
 */
package org.middleheaven.process;

/**
 * An attribute map-like context that organizes attributes names and values into scopes.
 * 
 */
public interface ScopedAttributesResolutionStrategy extends Iterable<Attribute> {

	/**
	 * @return the scope associated with this object.
	 */
	ContextScope getScope();

	
	/**
	 * Informs if the value of attributes can be read.
	 * @return <code>true</code> if the value of attributes can be read.
	 * 
	 */
	public boolean isReaddable();
	
	/**
	 * Informs if the value of attributes can be write.
	 * @return <code>true</code> if the value of attributes can be write.
	 * 
	 */
	public boolean isWritable();
	
	
	/**
	 * @return the quantity of attributes present in this scope.
	 */
	int size();

	/**
	 * @return <code>true</code> if this scope has no attributes. 
	 */
	boolean isEmpty();


	/**
	 * Obtains the value of the attribute by name. Then coerces the obtained value to the given classe, if possible.
	 * @param <T> the final value type.
	 * @param name the name of the attibute
	 * @param type the type of the attribue return value.
	 * @return the value of the attribute by name, or <code>null</code> if no attribute is present.
	 */
	public <T> T getAttribute(String name, Class<T> type);

	/**
	 * Set the value of the attribute in the scope. It does nothing if the attributes does not exist.
	 * 
	 * @param name
	 * 
	 * @throws {@link UnsupportedOperationException} if the scope is read only.
	 */
	public void setAttribute(String name, Object value);

	/**
	 * Removes the attribute from the scope. It does nothing if the attributes does not exist.
	 * 
	 * @param name
	 * 
	 * @throws {@link UnsupportedOperationException} if the scope is read only.
	 */
	public void removeAttribute(String name);

}