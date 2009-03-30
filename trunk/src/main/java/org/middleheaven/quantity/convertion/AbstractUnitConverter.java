package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.Unit;

public abstract class AbstractUnitConverter<E extends Measurable,T extends Scalable<E,T>> implements UnitConverter<E,T> {

	 Unit<E> originalUnit;
	 Unit<E> resultUnit;

	protected AbstractUnitConverter(Unit<E> originalUnit, Unit<E> resultUnit) {
		this.originalUnit = originalUnit;
		this.resultUnit = resultUnit;
	}
	
	@Override
	public final Unit<E> originalUnit() {
		return originalUnit;
	}

	@Override
	public final Unit<E> resultUnit() {
		return resultUnit;
	}

	public final UnitConverter<E,T> inverse(){
		return new InvertedUnitConverter<E,T>(this);
	}



}
