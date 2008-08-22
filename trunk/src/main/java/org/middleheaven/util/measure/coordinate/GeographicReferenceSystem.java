package org.middleheaven.util.measure.coordinate;

import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.measures.Distance;


public class GeographicReferenceSystem implements CoordinateReferenceSystem<GeoCoordinate> {

	static final DecimalMeasure<Distance> DISTANCE_TO_EARTH_CENTER_AT_ZERO_ALTITUDE = DecimalMeasure.exact(6378000, SI.METER);
	
	@Override
	public int getDimention() {
		return 3;
	}

	@Override
	public GeoCoordinate fromRetangular(RetangularCoordinate c) {
		SphericCoordinateReferenceSystem scs = new SphericCoordinateReferenceSystem();
		return this.fromSpheric(scs.fromRetangular(c));
	}

	@Override
	public RetangularCoordinate toRetangular(GeoCoordinate c) {
		SphericCoordinateReferenceSystem scs = new SphericCoordinateReferenceSystem();
		return scs.toRetangular(this.toSpheric(c));
	}

	public GeoCoordinate fromSpheric(SphericCoordinate c) {
		DecimalMeasure<Distance> r = c.radius().minus(DISTANCE_TO_EARTH_CENTER_AT_ZERO_ALTITUDE);
		return new GeoCoordinate(this, c.phi(), c.theta() , r  );
	}


	public SphericCoordinate toSpheric(GeoCoordinate c) {
		DecimalMeasure<Distance> r = c.getAltitude().plus(DISTANCE_TO_EARTH_CENTER_AT_ZERO_ALTITUDE);

		return new SphericCoordinate(new SphericCoordinateReferenceSystem(), r ,  c.getLatitude(), c.getLongitude()  );
	}
}
