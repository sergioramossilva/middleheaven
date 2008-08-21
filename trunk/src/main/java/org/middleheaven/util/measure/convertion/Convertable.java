package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Measurable;

public interface Convertable<E extends Measurable,  T> {

	public T convertTo(Unit<E> newUnit);
	public Unit<E> unit();	
}
