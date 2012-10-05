/**
 * 
 */
package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
public interface ValueResolver<F extends Field<F>> {

	
	public F resolve (int i,  Vector<F> original);
}
