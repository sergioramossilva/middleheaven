package org.middleheaven.test.global.location;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.global.location.WGM84;
import org.middleheaven.global.location.WorldGeodeticModel;
import org.middleheaven.util.measure.AngularMeasure;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.coordinate.GeoCoordinate;
import org.middleheaven.util.measure.measures.Distance;


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
