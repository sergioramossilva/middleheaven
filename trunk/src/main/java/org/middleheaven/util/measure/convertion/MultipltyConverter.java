package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.structure.Field;

public class MultipltyConverter<F extends Field<F>> extends AbstractUniConverter<F>{

	F factor;
	protected MultipltyConverter(Unit originalUnit, Unit resultUnit, F factor) {
		super(originalUnit, resultUnit);
		this.factor = factor;
	}

	@Override
	public F convert(F original) {
		return original.times(factor);
	}

	@Override
	public UnitConverter<F> inverse() {
		return new MultipltyConverter<F>(resultUnit, originalUnit , factor.inverse());
	}

}
