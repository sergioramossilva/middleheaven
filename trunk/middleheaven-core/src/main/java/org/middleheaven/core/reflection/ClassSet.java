package org.middleheaven.core.reflection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.middleheaven.core.reflection.inspection.Introspector;

/**
 * A set of classes.
 */
public final class ClassSet implements Iterable<Class<?>>{

	Set<Class<?>> types = new HashSet<Class<?>>();
	
	
	public ClassSet(){}
	
	/**
	 * Adds all classes in the package to this set.
	 * @param thePackage the package to analyze.
	 * @return <code>this</code> object
	 */
	public ClassSet add(Package thePackage){
		types.addAll(Introspector.of(thePackage).getClasses());

		return this;
	}
	
	public Set<Class<?>> getClasses(){
		return Collections.unmodifiableSet(types);
	}
	
	/**
	 * Adds a class to this set.
	 * @param theClass the class to add.
	 * @return <code>this</code> object
	 */
	public ClassSet add(Class<?> theClass){
		types.add(theClass);
		
		return this;
	}
	
	public ClassSet add(ClassSet other){
		types.addAll(other.types);
		
		return this;
	}

	@Override
	public Iterator<Class<?>> iterator() {
		return types.iterator();
	}
	
	public int size(){
		return types.size();
	}
}
