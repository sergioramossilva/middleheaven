package org.middleheaven.util.collections;

import java.util.Collection;

import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.Predicate;

public interface Walkable <T>{

	
	public Walkable<T> filter(Predicate<T> predicate);
	
	
	public <C> Walkable<C> map(Classifier <C, T> classifier);
	
	public void forEach(Walker<T> walker);
	
	public <L extends Collection<T>> L into(L collection);
}
