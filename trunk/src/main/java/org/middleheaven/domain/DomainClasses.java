package org.middleheaven.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionUtils;

public final class DomainClasses implements Iterable<Class<?>>{

	Set<Class<?>> entities = new HashSet<Class<?>>();
	
	public DomainClasses add(Package entitiesPackage){
		entities.addAll(ReflectionUtils.getPackageClasses(entitiesPackage));

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
