package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.Scalable;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Measurable;

public final class AditiveConverter<E extends Measurable> extends AbstractUnitConverter<E> {

	
	public static <E extends Measurable> AditiveConverter<E> convert(Unit<E> originalUnit,
			Unit<E> resultUnit, Real shift) {
		return new AditiveConverter<E>(shift,originalUnit, resultUnit );
	}
	
	private final Real shift;
	private AditiveConverter(Real shift , Unit<E> originalUnit, Unit<E> resultUnit) {
		super(originalUnit, resultUnit);
		this.shift = shift;
	}


	@Override
	public <T extends Scalable<E, T>> T convertFoward(T value) {
		if (!value.unit().equals(this.originalUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		final T diff =  value.one().times(shift, this.resultUnit);
		return value.times(Real.ONE(),  this.resultUnit).minus(diff);
	}



	@Override
	public <T extends Scalable<E, T>> T convertReverse(T value) {
		if (!value.unit().equals(this.resultUnit)){
			throw new IllegalArgumentException("Expected unit " + this.originalUnit + " but was " + value.unit());
		}
		final T diff =  value.one().times(shift, this.originalUnit);
		return value.times(Real.ONE(),  this.originalUnit).plus(diff);
	}



}
