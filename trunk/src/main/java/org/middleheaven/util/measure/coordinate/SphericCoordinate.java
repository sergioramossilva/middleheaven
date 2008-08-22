package org.middleheaven.util.measure.coordinate;

import org.middleheaven.util.measure.AngularPosition;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.measures.Distance;

public class SphericCoordinate extends Coordinate {

	
	protected SphericCoordinate(CoordinateReferenceSystem<SphericCoordinate> refSystem,
			DecimalMeasure<Distance> radius, AngularPosition phi, AngularPosition teta ) {
		super(refSystem, radius, phi,teta);
	}
	
	protected SphericCoordinate(CoordinateReferenceSystem<SphericCoordinate> refSystem,DecimalMeasure<?> ... coordinates) {
		super(refSystem, coordinates);
	}

	@SuppressWarnings("unchecked")
	public DecimalMeasure<Distance> radius(){
		return (DecimalMeasure<Distance>) this.getOrdinate(0);
	}
	
	public AngularPosition phi(){
		return (AngularPosition)this.getOrdinate(1);
	}
	
	public AngularPosition theta(){
		return (AngularPosition)this.getOrdinate(2);
	}
	

}
