/**
 * 
 */
package org.middleheaven.process.web.server.action.annotaions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.TYPE
	})
@Documented
public @interface Interceptors {

	Interceptor[] value();
}
