package org.middleheaven.domain.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.middleheaven.core.reflection.inspection.Introspector;

/**
 * A set of classes.
 */
public final class ClassSet implements Iterable<Class<?>>{

	Set<Class<?>> entities = new HashSet<Class<?>>();
	
	
	public ClassSet(){}
	
	/**
	 * Adds all classes in the package to this set.
	 * @param thePackage the package to analyze.
	 * @return <code>this</code> object
	 */
	public ClassSet add(Package thePackage){
		entities.addAll(Introspector.of(thePackage).getClasses());

		return this;
	}
	
	/**
	 * Adds a class to this set.
	 * @param theClass the class to add.
	 * @return <code>this</code> object
	 */
	public ClassSet add(Class<?> theClass){
		entities.add(theClass);
		
		return this;
	}

	@Override
	public Iterator<Class<?>> iterator() {
		return entities.iterator();
	}
}
