package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.Unit;

public final class AditiveConverter<E extends Measurable , T extends Scalable<E,T>> extends AbstractUnitConverter<E,T> {

	@SuppressWarnings("unchecked")
	public static <E extends Measurable> AditiveConverter<E,?> convert(Unit<E> originalUnit,
			Unit<E> resultUnit, Real shift) {
		return new AditiveConverter(shift,originalUnit, resultUnit );
	}
	
	private final Real shift;
	private AditiveConverter(Real shift , Unit<E> originalUnit, Unit<E> resultUnit) {
		super(originalUnit, resultUnit);
		this.shift = shift;
	}

	@Override
	public T convertFoward(T value) {
		if (!value.unit().equals(this.originalUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		final T diff =  value.one().times(shift, this.resultUnit);
		return value.times(Real.ONE(),  this.resultUnit).minus(diff);
	}

	@Override
	public T convertReverse(T value) {
		if (!value.unit().equals(this.resultUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		final T diff =  value.one().times(shift, this.originalUnit);
		return value.times(Real.ONE(),  this.originalUnit).plus(diff);
	}



}
