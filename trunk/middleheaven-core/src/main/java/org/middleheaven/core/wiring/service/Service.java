package org.middleheaven.core.wiring.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.core.wiring.annotations.ScopeSpecification;

@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.FIELD, 
	ElementType.PARAMETER,
	ElementType.TYPE
	})
@Documented
@Inherited
@ScopeSpecification
public @interface Service {

}