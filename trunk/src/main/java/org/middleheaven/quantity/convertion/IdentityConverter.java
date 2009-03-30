package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.Unit;

class IdentityConverter<E extends Measurable,T extends Scalable<E,T>> implements UnitConverter<E,T> {
	
	private Unit<E> unit;

	public IdentityConverter(Unit<E> unit) {
		super();
		this.unit = unit;
	}
	
	@Override
	public Unit<E> originalUnit() {
		return unit;
	}

	@Override
	public Unit<E> resultUnit() {
		return unit;
	}

	@Override
	public T convertFoward(T value) {
		return value;
	}

	@Override
	public T convertReverse(T value) {
		return value;
	}

	@Override
	public UnitConverter<E,T> inverse() {
		return this;
	}

}
