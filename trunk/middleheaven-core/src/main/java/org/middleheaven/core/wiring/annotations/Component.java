/**
 * 
 */
package org.middleheaven.core.wiring.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.core.wiring.WiringService;

/**
 * Indicates the class is a candidate to be add to the {@link WiringService} object context. 
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
@Inherited
public @interface Component {

}
