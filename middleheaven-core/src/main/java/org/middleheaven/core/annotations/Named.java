package org.middleheaven.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Add a name to a wiring specification allowing to differenced to wiring points of the same class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@Documented
public @interface Named {

	/**
	 * The name.
	 */
	String value();
}
