package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Scalable;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Measurable;


public interface UnitConverter<E extends Measurable> {

	/**
	 * Convert from measure given in unit A to measure in unit B
	 * where A.equals(this.originalUnit()) and B.equals(this.resultUnit()) 
	 * @param <M>
	 * @param original
	 * @return
	 */
	public <T extends Scalable<E,T>> T convertFoward( T value);
	public <T extends Scalable<E,T>> T convertReverse( T value);
	
	public UnitConverter<E> inverse();
 
	public Unit<E> originalUnit();

	public Unit<E> resultUnit();
}
