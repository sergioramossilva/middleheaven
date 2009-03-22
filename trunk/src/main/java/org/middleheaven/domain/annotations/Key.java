package org.middleheaven.domain.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD , ElementType.METHOD})
@Documented
public @interface Key {

	
	Class<? extends Identity> type() default IntegerIdentity.class;
}
