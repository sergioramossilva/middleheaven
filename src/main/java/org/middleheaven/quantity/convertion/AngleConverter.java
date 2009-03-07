package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Angle;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.SI;

public class AngleConverter extends AbstractUnitConverter<Angle> {

	protected AngleConverter() {
		super(SI.RADIANS, SI.DEGREE);
	}

	@Override
	public <T extends Scalable<Angle, T>> T convertFoward(T radians) {
		return radians.times(Real.valueOf(180),this.resultUnit).over(Real.valueOf(Math.PI), this.resultUnit);
	}

	@Override
	public <T extends Scalable<Angle, T>> T convertReverse(T degree) {
		return degree.times(Real.valueOf(Math.PI),this.originalUnit).over(Real.valueOf(180), this.originalUnit);
	}





}
