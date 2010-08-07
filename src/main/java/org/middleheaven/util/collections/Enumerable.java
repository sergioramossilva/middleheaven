package org.middleheaven.util.collections;

import org.middleheaven.util.classification.Classifier;

/**
 * Represents a unmodifiable collection-like interface that's still iterable and permits to query for the collection's size.
 * In addition provides several methods for manipulating the data in the collection. 
 *
 * @param <T>
 */
public interface Enumerable<T> extends Iterable<T>, Walkable<T>{
	
	/**
	 * 
	 * @return the number of elements in {@code this}.
	 */
	public int size();
	
	/**
	 * 
	 * @return <code>true</code> if the size is zero, <code>false</code> otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * Produces an {@code EnhancedCollection} of elements of {@code this} that are not rejected
	 * bu the given classifier.
	 * 
	 * @param classifier the rejection classifier. if the classifier returns <code>true</code> the element is reject and not added to the result collection
	 * @return an {@code EnhancedCollection} with the not rejected elements
	 */
	public EnhancedCollection<T> reject(Classifier<Boolean, T> classifier); 
	
	/**
	 * Counts the number of copies of object in the collection
	 * @param object the object to test
	 * @return the number of copies.
	 */
	public int count(T object);
	
	/**
	 * returns true if all items match the classifier
	 * @param classifier the classifier with the matching rule.
	 * @return <code>true</code> if all items match the classifier, <code>false</code> otherwise.
	 */
	public boolean every(Classifier<Boolean,T> classifier);
	
	/**
	 * returns true if any item matchs the classifier
	 * @param classifier the classifier with the matching rule.
	 * @return <code>true</code> if any item matchs the classifier, <code>false</code> otherwise.
	 */
	public boolean any(Classifier<Boolean,T> classifier);
	
	/**
	 * finds first item matching the classifier
	 * @param classifier the classifier with the matching rule.
	 * @return first item matching the classifier
	 */
	public T find(Classifier<Boolean,T> classifier);
	
	/**
	 * finds all items matching the classifier
	 * @param classifier the classifier with the matching rule.
	 * @return finds all items matching the classifier
	 */
	public EnhancedCollection<T> findAll(Classifier<Boolean,T> classifier);
	
	/**
	 * Collect the return value of calling a classifier on each item in a collection into a new collection.
	 * If the classifier returns {@code null} for a value, it will not be added to the result collection
	 * @param <C> the result type.
	 * @param classifier classifier the classifier with the matching rule.
	 * @return the collect itens.
	 */
	public <C> EnhancedCollection<C> collect(Classifier<C,T> classifier);
	
	/**
	 * Groups elemtens by classification C.
	 * 
	 * @param <C> the type of classification
	 * @param classifier  classifier the classifier with the classification rule.
	 * @return a {@code EnhancedMap} with keys being the classifications and values being a {@code EnhancedCollection} of elements
	 */
	public <C> EnhancedMap<C,EnhancedCollection<T>> groupBy(Classifier<C,T> classifier);

	/**
	 * Converters all elements to a string separated by a given separator. 
	 * Elements are converted invoking {@code toString()}.
	 * 
	 * @param separator separator used between elements
	 * @return a string of the String representation of the elements.
	 */
	public String join(String separator);
	
}
