package org.middleheaven.domain.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.domain.model.DomainClasses;

public final class DomainClasses implements Iterable<Class<?>>{

	Set<Class<?>> entities = new HashSet<Class<?>>();
	
	public DomainClasses add(Package entitiesPackage){
		entities.addAll(Introspector.of(entitiesPackage).getClasses());

		return this;
	}
	
	public DomainClasses add(Class<?> entityClass){
		entities.add(entityClass);
		
		return this;
	}

	@Override
	public Iterator<Class<?>> iterator() {
		return entities.iterator();
	}
}
