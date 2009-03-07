package org.middleheaven.quantity.coordinate;

import org.middleheaven.quantity.measure.DecimalMeasure;

public class RetangularCoordinate extends Coordinate{

	protected RetangularCoordinate(CoordinateReferenceSystem<RetangularCoordinate> refSystem,
			DecimalMeasure<?> ... coordinates) {
		super(refSystem, coordinates);
	}

	public DecimalMeasure<?> x(){
		return this.getOrdinate(0);
	}
	
	public DecimalMeasure<?> y(){
		return this.getOrdinate(1);
	}
	
	public DecimalMeasure<?> z(){
		return this.getOrdinate(2);
	}
}
