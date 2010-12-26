package org.middleheaven.domain.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a property as an email address
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD , ElementType.METHOD})
@Documented
public @interface Url {

}
