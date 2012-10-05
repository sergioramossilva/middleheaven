/**
 * 
 */
package org.middleheaven.model.annotations.mapping;

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
	
	String name();
	
	int length () default 0;
	
	int precision () default 0;
	
	boolean updatable() default true;

	boolean insertable() default true;

	boolean nullable () default true;

	int scale () default 0;
}
