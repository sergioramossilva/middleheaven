package org.middleheaven.core.wiring.annotations;

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
@BindingSpecification
@Documented
public @interface Wire {

	boolean required() default true;
	boolean shareabled() default true;
}
