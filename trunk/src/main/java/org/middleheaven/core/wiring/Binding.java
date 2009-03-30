package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class Binding {

	private Class<?> startType;
	private Class<?> scope;
	private Resolver resolver;
	private Set specifications = new HashSet();
	
	public String toString(){
		return startType.getName() + "+" + specifications.toString();
	}
	protected Class<?> getAbstractType() {
		return startType;
	}
	
	protected void setStartType(Class<?> startType) {
		this.startType = startType;
	}
	
	protected void setResolver(Resolver<?> resolver) {
		this.resolver = resolver;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Resolver<T> getResolver() {
		return resolver;
	}

	public Key getKey(){
		return Key.keyFor(this.startType, specifications);
	}


	public void addAnnotation(Class<? extends Annotation> type) {
		specifications.add(type);	
	}

	public void setTargetScope(Class<?> scope) {
		this.scope = scope;
	}
	
	public Class<?> getScope() {
		return scope;
	}
}
