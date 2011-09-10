package org.middleheaven.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface OneToOne {

	/**
	 * 
	 * @return the name of the field in this class that holds the 
	 * identity of the target instance
	 */
	String targetIdentityField() default "";
}
