/**
 * 
 */
package org.middleheaven.util.collections;

import java.util.Collection;

import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.Predicate;

/**
 * 
 */
public class IterableWalkable<T> implements Walkable<T> {

	
	private Iterable<T> iterable;

	public IterableWalkable (Iterable<T> iterable){
		this.iterable = iterable;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Walkable<T> filter(Predicate<T> predicate) {
		return new IterableWalkable<T>(new FilteredIterable<T>(iterable, predicate));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Walkable<C> map(Classifier<C, T> classifier) {
		return new IterableWalkable<C>(new TransformedIterable<T, C>(iterable, classifier));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Walker<T> walker) {
		for (T t : iterable){
			walker.doWith(t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<T>> L into(L collection) {
		for (T t : iterable){
			collection.add(t);
		}
		return collection;
	}

}
