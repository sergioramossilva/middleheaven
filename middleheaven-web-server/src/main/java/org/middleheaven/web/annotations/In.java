package org.middleheaven.web.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.process.ContextScope;

/**
 * Marks a method to be executed as the service method is it is a Get request
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface In {

	String value();
	ContextScope scope () default ContextScope.PARAMETERS;
}
