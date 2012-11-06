/**
 * 
 */
package org.middleheaven.process.web.server.action.annotaions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.core.annotations.Component;

/**
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.TYPE
	})
@Documented
@Component
public @interface Interceptor {
	
}
