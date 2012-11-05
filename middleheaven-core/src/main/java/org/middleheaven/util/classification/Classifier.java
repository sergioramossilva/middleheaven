package org.middleheaven.util.classification;

/**
 * Classifies an object of type <T> in a category <C>.
 * 
 * @param <C> category type
 * @param <T> object type
 */
public interface Classifier<C , T > {

	/**
	 * Classify the <code>obj</code> in a category.
	 * 
	 * @param obj the object to classify.
	 * @return the category of the object.
	 */
	public C classify (T obj); 
}
