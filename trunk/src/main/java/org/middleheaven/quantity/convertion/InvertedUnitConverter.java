package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.Unit;

public final class InvertedUnitConverter<E extends Measurable,T extends Scalable<E,T>> implements UnitConverter<E,T> {

	private UnitConverter<E,T> original;
	
	public InvertedUnitConverter(UnitConverter<E,T> original){
		this.original = original;
	}

	@Override
	public UnitConverter<E,T> inverse() {
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
	public T convertFoward(T value) {
		return original.convertReverse(value);
	}


	@Override
	public T convertReverse(T value) {
		return original.convertFoward(value);
	}

	

}
