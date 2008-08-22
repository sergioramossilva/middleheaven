package org.middleheaven.util.measure;

import org.middleheaven.util.measure.measures.Measurable;




/**
 * Base interface for the Quantity Pattern implementation
 * 
 * @author Sergio M.M. Taborda
 *
 */
public interface Quantity<E extends Measurable>{

	/**
	 * 
	 * @return this quantity unit
	 */
	public Unit<E> unit();
	
	
}
