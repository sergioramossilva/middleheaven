/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * 
 */
public interface ValueResolver<F extends FieldElement<F>> {

	
	public F resolve (int i,  Vector<F> original);
}
