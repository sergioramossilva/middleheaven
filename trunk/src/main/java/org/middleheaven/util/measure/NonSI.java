package org.middleheaven.util.measure;

import java.util.Collection;

import org.middleheaven.util.measure.measures.Measurable;
import org.middleheaven.util.measure.measures.Temperature;

public final class NonSI implements UnitSystem{

	public static final Unit<Temperature> CELSIUS = Unit.unit(Dimension.TEMPERATURE,"ºC");
	public static final Unit<Temperature> FAHRENHEIT = Unit.unit(Dimension.TEMPERATURE,"ºF");
	public static final Unit<Temperature> RANKINE = Unit.unit(Dimension.TEMPERATURE,"R");
	
	@Override
	public Unit getMeasuableUnit(Class<Measurable> measurable) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<Unit> units() {
		// TODO Auto-generated method stub
		return null;
	}

}
