package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.Unit;

public final class MultipltyConverter<E extends Measurable,T extends Scalable<E,T>> extends AbstractUnitConverter<E,T>{

	private Real factor;
	
	public static <E extends Measurable,I extends Scalable<E,I>> MultipltyConverter<E,I> convert(Unit<E> originalUnit, Unit<E> resultUnit,Real factor){
		return new MultipltyConverter<E,I>(originalUnit, resultUnit, factor);
	}

	private MultipltyConverter(Unit<E> originalUnit, Unit<E> resultUnit,Real factor) {
		super(originalUnit, resultUnit);
		this.factor = factor;
	}
	
	@Override
	public T convertFoward(T value) {
		if (!value.unit().equals(this.originalUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		return value.times(factor,this.resultUnit);
	}

	@Override
	public T convertReverse(T value) {
		if (!value.unit().equals(this.resultUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		return value.over(factor,this.originalUnit);
	}
	



}
