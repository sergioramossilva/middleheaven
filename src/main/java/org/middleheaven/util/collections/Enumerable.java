package org.middleheaven.util.collections;

import org.middleheaven.util.classification.Classifier;

public interface Enumerable<T> extends Iterable<T>, Walkable<T>{
	
	public int size();
	public boolean isEmpty();
	
	public Enumerable<T> reject(Classifier<Boolean, T> classifier); 
	
	/**
	 * Counts the number of copies of object in the collection
	 * @param object
	 * @return
	 */
	public int count (T object);
	
	/**
	 * returns true if all items match the classifier
	 * @param classifier
	 * @return
	 */
	public boolean every(Classifier<Boolean,T> classifier);
	
	/**
	 * returns true if any item match the classifier
	 * @param classifier
	 * @return
	 */
	public boolean any(Classifier<Boolean,T> classifier);
	
	/**
	 * finds first item matching the classifier
	 * @param classifier
	 * @return
	 */
	public T find (Classifier<Boolean,T> classifier);
	
	/**
	 * finds all items matching the classifier
	 * @param classifier
	 * @return
	 */
	public EnhancedCollection<T> findAll (Classifier<Boolean,T> classifier);
	
	/**
	 * Collect the return value of calling a classifier on each item in a collection into a new collection.
	 * If the classifier returns {@code null} for a value, it will not be added to the result collection
	 * @param classifier
	 * @return
	 */
	public <C> EnhancedCollection<C> collect (Classifier<C,T> classifier);
	
	public <C> EnhancedMap<C,T> groupBy(Classifier<C,T> classifier);

	
	public String join(String separator);
	
}
