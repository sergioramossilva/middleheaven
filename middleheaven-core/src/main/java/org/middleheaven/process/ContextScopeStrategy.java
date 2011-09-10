/**
 * 
 */
package org.middleheaven.process;


public interface ContextScopeStrategy extends Iterable<Attribute> {

	/**
	 * @return
	 */
	int size();

	/**
	 * @return
	 */
	boolean isEmpty();

	/**
	 * @return
	 */
	ContextScope getScope();

	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param type
	 * @return
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