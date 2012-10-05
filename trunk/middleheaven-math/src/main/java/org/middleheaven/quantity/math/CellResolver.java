/**
 * 
 */
package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
public interface CellResolver<F extends Field<F>> {

	
	public F resolve (int r, int c, Matrix<F> original);
}
