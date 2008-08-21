package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Measurable;

public abstract class AbstractUnitConverter<E extends Measurable> implements UnitConverter<E> {

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

	public final UnitConverter<E> inverse(){
		return new InvertedUnitConverter<E>(this);
	}



}
