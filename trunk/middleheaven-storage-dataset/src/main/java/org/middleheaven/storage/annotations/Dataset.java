/**
 * 
 */
package org.middleheaven.storage.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add dataset related information
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Dataset {

	/**
	 * Logical reference name for the dataset.
	 * @return Logical reference name for the dataset.
	 */
	String hardName() default "";


}
