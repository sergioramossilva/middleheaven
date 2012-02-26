package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;


public interface Binder {


	public <T> BindingBuilder<T> bind(Class<T> type);
	
	public <S extends ScopePool> void bindScope(Class<? extends Annotation> annotation , Class<S> scope);
	public <T> PropertyBindingBuilder<T> bindProperty(Class<T> type);

}
