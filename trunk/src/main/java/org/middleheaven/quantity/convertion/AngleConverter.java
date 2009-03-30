package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Angle;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.SI;

public class AngleConverter<T extends Scalable<Angle,T>> extends AbstractUnitConverter<Angle, T> {

	protected AngleConverter() {
		super(SI.RADIANS, SI.DEGREE);
	}


	public T convertFoward(T radians) {
		return radians.times(Real.valueOf(180),this.resultUnit).over(Real.valueOf(Math.PI), this.resultUnit);
	}


	public T convertReverse(T degree) {
		return degree.times(Real.valueOf(Math.PI),this.originalUnit).over(Real.valueOf(180), this.originalUnit);
	}





}
