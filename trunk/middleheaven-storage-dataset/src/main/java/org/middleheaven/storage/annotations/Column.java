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
 * Market annotation that signifies the fields is
 * not to be preserved 
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.METHOD , ElementType.FIELD
	})
@Documented
public @interface Column {
	

	String hardName() default "";
	
	/**
	 * The total number of characters. Only applies to text based columns.
	 * @return The total number of characters
	 */
	int length () default 0;
	
	/**
	 * The total number of decimal digits 
	 * @return The total number of decimal digits 
	 */
	int precision () default 0; // 0 means defaults to an integer
	
	boolean updatable() default true;

	boolean insertable() default true;

	boolean nullable () default true;

	/**
	 * The total number of digits (counting decimal ones)
	 * @return The total number of digits (counting decimal ones)
	 */
	int scale () default 0; // means it default to none. this will rise a model exception if the column is numeric
}
