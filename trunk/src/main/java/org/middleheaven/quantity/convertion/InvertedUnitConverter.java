package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.Unit;

public final class InvertedUnitConverter<E extends Measurable> implements UnitConverter<E> {

	private UnitConverter<E> original;
	
	public InvertedUnitConverter(UnitConverter<E> original){
		this.original = original;
	}
	

	@Override
	public UnitConverter<E> inverse() {
		return original;
	}

	@Override
	public Unit<E> originalUnit() {
		return original.resultUnit();
	}

	@Override
	public Unit<E> resultUnit() {
		return original.originalUnit();
	}


	@Override
	public <T extends Scalable<E, T>> T convertFoward(T value) {
		return original.convertReverse(value);
	}


	@Override
	public <T extends Scalable<E, T>> T convertReverse(T value) {
		return original.convertFoward(value);
	}

	

}
