package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.Unit;

/**
 * Converts units 
 *
 * @param <E>
 */
public interface UnitConverter<E extends Measurable,T extends Scalable<E,T>> {

	/**
	 * Convert from measure given in unit A to measure in unit B
	 * where A.equals(this.originalUnit()) and B.equals(this.resultUnit()) 
	 * @param original
	 * @param <M>
	 * @return
	 */
	public T convertFoward( T value);
	public T convertReverse( T value);
	
	public UnitConverter<E,T> inverse();
 
	public Unit<E> originalUnit();

	public Unit<E> resultUnit();
}
