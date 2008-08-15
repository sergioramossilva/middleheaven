package org.middleheaven.global.location;

import org.middleheaven.util.measure.AngularPosition;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.measures.Distance;

public class Coordinates {

	private AngularPosition latitude;
	private AngularPosition longitude;
	private DecimalMeasure altitude;
	
	public Coordinates(AngularPosition latitude, AngularPosition longitude ){
		this(latitude, longitude,DecimalMeasure.exact(Real.ZERO(), SI.METER) );
	}
	
	public Coordinates(AngularPosition latitude, AngularPosition longitude,DecimalMeasure altitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}
	
	public AngularPosition getLatitude() {
		return latitude;
	}
	public AngularPosition getLongitude() {
		return longitude;
	}
	public DecimalMeasure<Distance> getAltitude() {
		return altitude;
	}
}
