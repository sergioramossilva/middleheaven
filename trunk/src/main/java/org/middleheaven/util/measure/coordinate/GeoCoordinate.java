package org.middleheaven.util.measure.coordinate;

import org.middleheaven.util.measure.AngularPosition;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.measures.Distance;
import org.middleheaven.util.measure.time.TimeZone;

public class GeoCoordinate extends Coordinate{

	public GeoCoordinate(AngularPosition latitude, AngularPosition longitude ){
		this(new GeographicReferenceSystem() ,  latitude, longitude,DecimalMeasure.exact(Real.ZERO(), SI.METER));
	}
	
	public GeoCoordinate(CoordinateReferenceSystem<GeoCoordinate> refSystem, 
			AngularPosition latitude,
			AngularPosition longitude,
			DecimalMeasure<Distance> height) {
		super(refSystem, height ,latitude , longitude );
	}
	
	protected GeoCoordinate(CoordinateReferenceSystem<GeoCoordinate> refSystem,
			DecimalMeasure<?> ... coordinates) {
		super(refSystem, coordinates);
	}

	
	@SuppressWarnings("unchecked")
	public DecimalMeasure<Distance> getAltitude(){
		return (DecimalMeasure<Distance>) this.getOrdinate(0);
	}
	/**
	 * Unsigned latitude position
	 * @return
	 */
	public AngularPosition getLatitude(){
		return (AngularPosition)this.getOrdinate(1);
	}
	
	/**
	 * Unsigned longitude position
	 * @return
	 */
	public AngularPosition getLongitude(){
		return (AngularPosition)this.getOrdinate(2);
	}
	
	/**
	 * Signed latitude position
	 * @return
	 */
	public AngularPosition getRelativeLatitude(){
		
		double angle = this.getLatitude().toDegrees().amount().asNumber().doubleValue();
		if (angle > 180){
			angle = angle-360;
		}
		
		return AngularPosition.degrees(angle);
	}
	
	/**
	 * Signed longitude position
	 * @return
	 */
	public AngularPosition getRelativeLongitude(){
		
		double angle = this.getLongitude().toDegrees().amount().asNumber().doubleValue();
		if (angle > 180){
			angle = angle-360;
		}
		
		return AngularPosition.degrees(angle);
	}
	
	public TimeZone getGMTTimeZone(){
		final int hours = this.getRelativeLongitude().toDegrees().amount().asNumber().intValue()/15;
		return TimeZone.getGMTTimeZone(hours);
	}
}
