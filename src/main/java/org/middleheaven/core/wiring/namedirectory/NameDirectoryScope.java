package org.middleheaven.core.wiring.namedirectory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
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
@ScopeSpecification
public @interface NameDirectoryScope {

}
