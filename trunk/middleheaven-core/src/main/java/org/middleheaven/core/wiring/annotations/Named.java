package org.middleheaven.core.wiring.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add a name to a wiring specification allowing to differenced to wiring points of the same class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.FIELD, 
	ElementType.PARAMETER
	})
@BindingSpecification
@Documented
public @interface Named {

	/**
	 * The name.
	 */
	String value();
}
