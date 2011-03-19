package org.middleheaven.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ManyToMany {

	/**
	 * 
	 * @return end point entity class
	 */
	Class<?> target();
	
	/**
	 * 
	 * @return Entity class that acts as mediator
	 */
	Class<?> mediator();
	
}
