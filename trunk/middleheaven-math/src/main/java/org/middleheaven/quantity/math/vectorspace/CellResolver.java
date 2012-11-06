/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * 
 */
public interface CellResolver<F extends FieldElement<F>> {

	
	public F resolve (int r, int c, Matrix<F> original);
}
