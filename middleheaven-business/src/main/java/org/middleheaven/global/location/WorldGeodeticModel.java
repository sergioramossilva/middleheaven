package org.middleheaven.global.location;

import org.middleheaven.quantity.coordinate.GeoCoordinate;
import org.middleheaven.quantity.measurables.Distance;
import org.middleheaven.quantity.measure.DecimalMeasure;

public abstract class WorldGeodeticModel {

	public abstract DecimalMeasure<Distance> distance(GeoCoordinate c1, GeoCoordinate c2);
	
}
