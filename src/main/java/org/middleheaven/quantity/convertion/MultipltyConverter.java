package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.Unit;

public final class MultipltyConverter<E extends Measurable> extends AbstractUnitConverter<E>{

	private Real factor;
	
	public static <T,E extends Measurable> MultipltyConverter<E> convert(Unit<E> originalUnit, Unit<E> resultUnit,Real factor){
		return new MultipltyConverter<E>(originalUnit, resultUnit, factor);
	}

	private MultipltyConverter(Unit<E> originalUnit, Unit<E> resultUnit,Real factor) {
		super(originalUnit, resultUnit);
		this.factor = factor;
	}
	
	@Override
	public <T extends Scalable<E, T>> T convertFoward(T value) {
		if (!value.unit().equals(this.originalUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		return value.times(factor,this.resultUnit);
	}

	@Override
	public <T extends Scalable<E, T>> T convertReverse(T value) {
		if (!value.unit().equals(this.resultUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		return value.over(factor,this.originalUnit);
	}
	



}
