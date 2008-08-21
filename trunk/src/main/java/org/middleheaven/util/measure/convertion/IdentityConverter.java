package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Scalable;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Measurable;

class IdentityConverter<E extends Measurable> implements UnitConverter<E> {

	
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
	public <T extends Scalable<E, T>> T convertFoward(T value) {
		return value;
	}

	@Override
	public <T extends Scalable<E, T>> T convertReverse(T value) {
		return value;
	}

	@Override
	public UnitConverter<E> inverse() {
		return this;
	}

}
