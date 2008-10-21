package org.middleheaven.storage.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.data.DataType;

/**
 * Market annotation that signifies the fields is
 * not to be preserved 
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.FIELD
	})
@Documented
public @interface Column {
	
	String value();
	int length () default 100;
	
	/**
	 * Idicates whether the field is to be persisted
	 * @return
	 */
	boolean persistable () default true;
	
	DataType type() default DataType.UNKWON;

}
