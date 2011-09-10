package org.middleheaven.core.reflection.metaclass;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.PropertyNotFoundException;
import org.middleheaven.util.collections.Enumerable;

/**
 * An interface for reading meta information from a class.
 */
public interface MetaClass {

	/**
	 * Retrieves the PropertyAcessor of the property with the given name
	 * @param name the property name.
	 * @return the property assessor.
	 * @throws PropertyNotFoundException if the property does not exist in the bean
	 */
	public PropertyAccessor getPropertyAcessor(String name) throws PropertyNotFoundException;
	
	/**
	 * Retrieves all assessors for all the existing properties.
	 * @return an enumerable with all the acessors.
	 */
	public Enumerable<PropertyAccessor> getProperties();
	
	/**
	 * Creates a new object based on the classe.
	 * @return the new object instance.
	 */
	public MetaBean newInstance();
	
	/**
	 * The class simple name. 
	 * Equivalent to {@link java.lang.Class#getSimpleName}
	 * @return the class simple name.
	 */
	public String getSimpleName();
	
	/**
	 * The class full qualified name. 
	 * Equivalent to {@link java.lang.Class#getName}
	 * @return the class full qualified name.
	 */
	public String getName();
	
	/**
	 * 
	 * @param name the property name
	 * @return <code>true</code> if this MetaClass contains this property acessor.
	 */
	public boolean containsProperty(String name);
}
