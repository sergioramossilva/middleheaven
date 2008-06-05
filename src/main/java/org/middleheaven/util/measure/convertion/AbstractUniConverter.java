package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.structure.Field;

public abstract class AbstractUniConverter<F extends Field<F>> implements UnitConverter<F> {

	Unit originalUnit;
	Unit resultUnit;

	protected AbstractUniConverter(Unit originalUnit, Unit resultUnit) {
		this.originalUnit = originalUnit;
		this.resultUnit = resultUnit;
	}
	
	@Override
	public Unit originalUnit() {
		return originalUnit;
	}

	@Override
	public Unit resultUnit() {
		return resultUnit;
	}




}
