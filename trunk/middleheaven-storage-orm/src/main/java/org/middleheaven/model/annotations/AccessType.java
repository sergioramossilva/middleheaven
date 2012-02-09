/**
 * 
 */
package org.middleheaven.model.annotations;

/**
 * The property access types.
 * Qualify how the values will be write an read.
 */
public enum AccessType {

	/**
	 * Access to property values is made by direct call to the corresponding inner field. 
	 */
	FIELD,
	/**
	 * Access to property values is made thru get/set invocation. 
	 */
	PROPERTY
}
