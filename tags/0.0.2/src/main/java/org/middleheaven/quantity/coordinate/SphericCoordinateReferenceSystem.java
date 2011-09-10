package org.middleheaven.quantity.coordinate;

import org.middleheaven.quantity.measure.AngularMeasure;
import org.middleheaven.quantity.measure.DecimalMeasure;

public class SphericCoordinateReferenceSystem implements CoordinateReferenceSystem<SphericCoordinate>{

	@Override
	public int getDimention() {
		return 3;
	}

	@Override
	public RetangularCoordinate toRetangular(SphericCoordinate c) {
	
		DecimalMeasure<?> r = c.radius();
		AngularMeasure teta = c.theta();
		AngularMeasure phi = c.phi();
		
		//x = r sen teta cos phi
		//y = r sen teta sin phi
		//z = r cos teta 
		
		DecimalMeasure<?> x = r.times(teta.sin()).times(phi.cos());
		DecimalMeasure<?> y = r.times(teta.sin()).times(phi.sin());
		DecimalMeasure<?> z = r.times(teta.cos());
		
		return new RetangularCoordinate(new RetangularCoordinateSystem(), x,y,z);
	}

	@Override
	public SphericCoordinate fromRetangular(RetangularCoordinate c) {

		RetangularCoordinate cc = (RetangularCoordinate)c;
		
		DecimalMeasure<?> x = cc.x();
		DecimalMeasure<?> y = cc.y();
		DecimalMeasure<?> z = cc.z();
		
		DecimalMeasure<?> r = x.times(x).plus(y.times(y)).plus(z.times(z)).sqrt(x.unit());
		AngularMeasure teta =  AngularMeasure.arctan(x.times(x).plus(y.times(y)).sqrt(x.unit()).over(r));
		AngularMeasure phi = AngularMeasure.arctan(x.over(y));
		
		return new SphericCoordinate(this, r, teta, phi);
	}
}
