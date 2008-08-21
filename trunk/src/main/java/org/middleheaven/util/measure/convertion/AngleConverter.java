package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.Scalable;
import org.middleheaven.util.measure.measures.Angle;

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
