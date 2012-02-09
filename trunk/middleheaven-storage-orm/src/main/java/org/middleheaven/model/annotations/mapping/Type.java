/**
 * 
 */
package org.middleheaven.model.annotations.mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.storage.types.TypeMapper;

/**
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD , ElementType.FIELD })
@Documented
public @interface Type {

	
	Class<TypeMapper> type ();
}
