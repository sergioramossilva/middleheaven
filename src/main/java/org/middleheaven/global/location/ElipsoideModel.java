package org.middleheaven.global.location;

import org.middleheaven.quantity.measurables.Dimensionless;
import org.middleheaven.quantity.measurables.Distance;
import org.middleheaven.quantity.measure.DecimalMeasure;

public abstract class ElipsoideModel extends WorldGeodeticModel{

	private DecimalMeasure<Distance> semiMajorAxis;
	private DecimalMeasure<Distance> semiMinorAxis;
	private DecimalMeasure<Dimensionless> flatening;
	

	public ElipsoideModel(DecimalMeasure<Distance> semiMajorAxis, DecimalMeasure<Distance> semiMinorAxis,
			DecimalMeasure<Dimensionless> flatening) {
		super();
		this.semiMajorAxis = semiMajorAxis;
		this.semiMinorAxis = semiMinorAxis;
		this.flatening = flatening;
	}


	public DecimalMeasure<Distance> getSemiMajorAxis() {
		return semiMajorAxis;
	}


	public DecimalMeasure<Distance> getSemiMinorAxis() {
		return semiMinorAxis;
	}


	public DecimalMeasure<Dimensionless> getFlatening() {
		return flatening;
	}
	


}
