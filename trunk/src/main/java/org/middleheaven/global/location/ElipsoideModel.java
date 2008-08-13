package org.middleheaven.global.location;

import org.middleheaven.util.measure.DecimalMeasure;

public abstract class ElipsoideModel extends WorldGeodeticModel{

	private DecimalMeasure semiMajorAxis;
	private DecimalMeasure semiMinorAxis;
	private DecimalMeasure flatening;
	

	public ElipsoideModel(DecimalMeasure semiMajorAxis, DecimalMeasure semiMinorAxis,
			DecimalMeasure flatening) {
		super();
		this.semiMajorAxis = semiMajorAxis;
		this.semiMinorAxis = semiMinorAxis;
		this.flatening = flatening;
	}


	public DecimalMeasure getSemiMajorAxis() {
		return semiMajorAxis;
	}


	public DecimalMeasure getSemiMinorAxis() {
		return semiMinorAxis;
	}


	public DecimalMeasure getFlatening() {
		return flatening;
	}
	


}
