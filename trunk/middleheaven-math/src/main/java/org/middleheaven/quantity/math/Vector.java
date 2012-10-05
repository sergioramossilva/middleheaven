/**
 * 
 */
package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.VectorSpace;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.Walkable;

/**
 * 
 */
public interface Vector<F extends Field<F>> extends VectorSpace<Vector<F>,F> , Walkable<F>{
	
	public F get(int index);
	
	public int size();

	public F[] toArray(F[] elements);
	
	/**
	 * Dot product for the vector.
	 * 
	 * @param other
	 * @return
	 */
	public F times(Vector<F> other);

	/**
	 * Multiplies this vector by other vector point by point 
	 * 
	 * @param other
	 * @return  result[i] = this[i] * other[i]
	 * 
	 * 
	 */
	public Vector<F> multiply(Vector<F> other);
	
	
	public <N extends Field<N>> Vector<N> apply(UnivariateFunction <F, N> function);
}
