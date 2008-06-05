package org.middleheaven.util.measure.coordinate;

import org.middleheaven.util.measure.Measure;
import org.middleheaven.util.measure.structure.Field;

public class LatLong extends Coordinate {

	
	
	public static LatLong coordinatesOf(Measure longitude, Measure latitude){
		GeographicReferenceSystem grs = new GeographicReferenceSystem();
		
		return grs.coordinateAt(ordinates);
	}
	
	protected LatLong(CoordinateReferenceSystem refSystem,Measure[] coordinates) {
		super(refSystem, coordinates);
	}

	public Latitude getLatitude(){
		this.getOrdinate(0)
	}
}
