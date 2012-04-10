/**
 * 
 */
package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;

import org.middleheaven.core.wiring.annotations.ScopeSpecification;

/**
 * 
 */
public class WiringUtils {

	
	public static String readScope(Annotation a){
		return readScope(a.annotationType());
	}
	
	public static String readScope(Class<? extends Annotation> annotationType){
		final ScopeSpecification scopeSpecification = annotationType.getAnnotation(ScopeSpecification.class);
		
		if (scopeSpecification != null){
			
			return scopeSpecification.name();
		} 
		
		return null;
	}
}
