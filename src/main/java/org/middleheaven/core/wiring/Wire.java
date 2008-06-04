package org.middleheaven.core.wiring;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Declares the need for the injection toolbox to operate 
 * on the declared member
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.CONSTRUCTOR,
	ElementType.METHOD,
	ElementType.FIELD, 
	ElementType.PARAMETER,
	ElementType.TYPE
	})
@Documented
public @interface Wire {

	boolean optional() default false;
}
