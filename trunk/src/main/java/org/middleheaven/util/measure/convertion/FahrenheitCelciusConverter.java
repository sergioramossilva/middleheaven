package org.middleheaven.util.measure.convertion;

import org.middleheaven.util.measure.NonSI;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.Scalable;
import org.middleheaven.util.measure.measures.Temperature;

public class FahrenheitCelciusConverter extends AbstractUnitConverter<Temperature> {

	protected FahrenheitCelciusConverter() {
		super(NonSI.FAHRENHEIT, NonSI.CELSIUS);
	}

	@Override
	public <T extends Scalable<Temperature, T>> T convertFoward(T farhrenheit) {
		final T ZERO_CELCIUS =  farhrenheit.one().times(Real.valueOf(32), farhrenheit.unit());
		return farhrenheit.minus(ZERO_CELCIUS).times(Real.valueOf(5),this.resultUnit).over(Real.valueOf(9), this.resultUnit);
	}

	@Override
	public <T extends Scalable<Temperature, T>> T convertReverse(T celcius) {
		final T ZERO_CELCIUS =  celcius.one().times(Real.valueOf(32),this.originalUnit);
		return celcius.times(Real.fraction(9,5),this.originalUnit).plus(ZERO_CELCIUS);
	}





}
