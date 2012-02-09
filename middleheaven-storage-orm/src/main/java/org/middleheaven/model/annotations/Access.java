/**
 * 
 */
package org.middleheaven.model.annotations;

/**
 * Qualify how the values will be write an read.
 */
public @interface Access {
	
	AccessType type() default AccessType.FIELD;

}
