/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import java.io.Serializable;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * 
 */
public interface VectorSpaceProvider extends Serializable{
	

	/**
	 * 
	 * @param field
	 * @return
	 */
	public <F extends FieldElement<F>> VectorSpace<Vector<F>, F> getVectorSpaceOver(Field<F> field, int dimensions);
	
}
