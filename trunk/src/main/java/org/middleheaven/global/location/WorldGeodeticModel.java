package org.middleheaven.global.location;

import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.coordinate.GeoCoordinate;
import org.middleheaven.util.measure.measures.Distance;

public abstract class WorldGeodeticModel {

	public abstract DecimalMeasure<Distance> distance(GeoCoordinate c1, GeoCoordinate c2);
	
}
