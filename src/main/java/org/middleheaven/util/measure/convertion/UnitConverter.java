package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.structure.Field;


public interface UnitConverter<F extends Field<F>> {

	public F convert(F original);

	public UnitConverter<F> inverse();

	public Unit originalUnit();

	public Unit resultUnit();
}
