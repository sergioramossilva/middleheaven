package org.middleheaven.global.location;

import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.time.Period;

public interface LocationProvider {

	public void addLocationListener (LocationListener listener , Period interval, Period timeout, Period maxAge);
	
	public void addProximityListener (ProximityListener listener , DecimalMeasure radius);
	
}
