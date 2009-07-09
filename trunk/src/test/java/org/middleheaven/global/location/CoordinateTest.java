package org.middleheaven.global.location;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.global.location.WGM84;
import org.middleheaven.global.location.WorldGeodeticModel;
import org.middleheaven.quantity.coordinate.GeoCoordinate;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Distance;
import org.middleheaven.quantity.measure.AngularMeasure;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.SI;


public class CoordinateTest {

	
	@Test 
	public void testDistance(){
		
		WorldGeodeticModel model = new WGM84();
		
		GeoCoordinate c1 = new GeoCoordinate(
				AngularMeasure.degrees(53),
				AngularMeasure.degrees(2)
		);
				
		GeoCoordinate c2 = new GeoCoordinate(
				AngularMeasure.degrees(52),
				AngularMeasure.degrees(0)
		);
			
		DecimalMeasure<Distance> distance = DecimalMeasure.exact(Real.valueOf(175572.473), SI.METER); 
		assertEquals(distance, model.distance(c1, c2));
		
		
		 c1 = new GeoCoordinate(
				AngularMeasure.degrees(0),
				AngularMeasure.degrees(0)
		);
				
		 c2 = new GeoCoordinate(
				AngularMeasure.degrees(52),
				AngularMeasure.degrees(0)
		);
		
	    distance = DecimalMeasure.exact(Real.valueOf(5763343.550), SI.METER); 
		assertEquals(distance, model.distance(c1, c2));
		
	}
}