package org.middleheaven.quantity.measure;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.structure.GroupAdditive;
import org.middleheaven.quantity.unit.Unit;

public interface Scalable<E extends Measurable,T extends Scalable<E,T>> extends GroupAdditive<T> {

	public T over(Real other, Unit<E> unit);
	public T times(Real other, Unit<E> unit);
	public Unit<E> unit();
	public T one();
}