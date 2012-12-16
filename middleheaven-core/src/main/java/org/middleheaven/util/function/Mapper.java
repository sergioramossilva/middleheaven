package org.middleheaven.util.function;

/**
 * Mapps an object of type <T> in a category <C>.
 * 
 * @param <C> category type
 * @param <T> object type
 */
public interface Mapper<C , T > extends Function<C, T>{

	/**
	 * Maps the given <code>obj</code> into a category
	 * 
	 * @param obj the object to classify.
	 * @return the category of the object.
	 */
	public C apply (T obj); 
}
