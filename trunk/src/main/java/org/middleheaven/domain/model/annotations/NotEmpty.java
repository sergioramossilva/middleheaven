package org.middleheaven.domain.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * For {@code CharSequence} properties indicates there must be at least a non blanck character in the sequence.
 * For {@code Collection} or {@code Map} properties indicates there must be at least one element in the set.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD , ElementType.METHOD})
@Documented
public @interface NotEmpty {

}
