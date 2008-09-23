package org.middleheaven.util.measure.coordinate;

import org.middleheaven.util.measure.AngularMeasure;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.measures.Distance;

public class SphericCoordinate extends Coordinate {

	
	protected SphericCoordinate(CoordinateReferenceSystem<SphericCoordinate> refSystem,
			DecimalMeasure<Distance> radius, AngularMeasure phi, AngularMeasure teta ) {
		super(refSystem, radius, phi,teta);
	}
	
	protected SphericCoordinate(CoordinateReferenceSystem<SphericCoordinate> refSystem,DecimalMeasure<?> ... coordinates) {
		super(refSystem, coordinates);
	}

	@SuppressWarnings("unchecked")
	public DecimalMeasure<Distance> radius(){
		return (DecimalMeasure<Distance>) this.getOrdinate(0);
	}
	
	public AngularMeasure phi(){
		return (AngularMeasure)this.getOrdinate(1);
	}
	
	public AngularMeasure theta(){
		return (AngularMeasure)this.getOrdinate(2);
	}
	

}
