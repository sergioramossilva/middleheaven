package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.Unit;

public class AngleConverter extends AbstractUniConverter<Real>{

	protected AngleConverter(Unit originalUnit, Unit resultUnit) {
		super(originalUnit, resultUnit);
	}

	@Override
	public Real convert(Real original) {
		
		if (original.unit().equals(resultUnit)){
			return original;
		} else if (resultUnit.equals(SI.RADIANS)){
			return Real.valueOf(Math.toRadians(original.asNumber().doubleValue()));
		} else {
			return Real.valueOf(Math.toDegrees(original.asNumber().doubleValue()));
		}
	}

	@Override
	public UnitConverter<Real> inverse() {
		return new AngleConverter(resultUnit,originalUnit);
	}



}
