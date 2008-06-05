package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.structure.Field;

public interface Convertable<F extends Field<F>, T> {

	public T convert(UnitConverter<F> converter, Unit newUnit);
	public Unit unit();	
}
