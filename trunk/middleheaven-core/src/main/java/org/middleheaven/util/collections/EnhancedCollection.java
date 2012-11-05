package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.Random;

import org.middleheaven.util.classification.Classification;
import org.middleheaven.util.classification.Classifier;

/**
 * An {@link Enumerable} enhanced  {@link Collection}.
 * @param <T> the type of the item in the collection.
 */
public interface EnhancedCollection<T> extends Collection<T> , Enumerable<T> {

	public EnhancedCollection<T> asUnmodifiable();
	
	public EnhancedCollection<T> asSynchronized();
	
	/**
	 * Select an item from the collection at random.
	 * @param random a {@link Random} instance
	 * @return an item from the collection at random.
	 * 
	 */
	public T random(Random random);

	/**
	 * Select an item from the collection at random.
	 * @return an item from the collection at random.
	 */
	public T random();
	
	public EnhancedList<T>  sort();

	public EnhancedCollection<T> shuffle();
	public EnhancedCollection<T> shuffle(Random random);
	
	public EnhancedCollection<T> intersect(EnhancedCollection<T> other);
	
	public EnhancedCollection<T> union(EnhancedCollection<T> other);
	
	public EnhancedList<T> sort(Comparator<? super T> comparator);
	
	public <C> Classification<C,T> classify(Classifier<C,T> classifier);

	public boolean containsSame(Collection<T> other);

	public EnhancedList<T> asList();
	
	/**
	 * Converts this collection to a {@link EnhancedSet}.
	 * @return
	 */
	public EnhancedSet<T> asSet();

	/**
	 * Get the first element in the collection or <code>null</code> if the collection is empty. The first element is defined
	 * by colelction's iterator's order.
	 * @return the first element in the collection or <code>null</code> if the collection is empty.
	 */
	public T getFirst();
	

}
