package org.middleheaven.quantity.coordinate;

import org.middleheaven.quantity.measurables.Distance;
import org.middleheaven.quantity.measure.AngularMeasure;
import org.middleheaven.quantity.measure.DecimalMeasure;

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
