package org.middleheaven.util.measure;

import org.middleheaven.util.measure.measures.Measurable;
import org.middleheaven.util.measure.structure.GroupAdditive;

public interface Scalable<E extends Measurable,T extends Scalable<E,T>> extends GroupAdditive<T> {

	public T over(Real other, Unit<E> unit);
	public T times(Real other, Unit<E> unit);
	public Unit<E> unit();
	public T one();
}