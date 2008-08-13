package org.middleheaven.global.location;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.util.measure.AngularPosition;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;


public class CoordinateTest {

	
	@Test 
	public void testDistance(){
		
		WorldGeodeticModel model = new WGM84();
		
		Coordinates c1 = new Coordinates(
				AngularPosition.degrees(53),
				AngularPosition.degrees(2)
		);
				
		Coordinates c2 = new Coordinates(
				AngularPosition.degrees(52),
				AngularPosition.degrees(0)
		);
			
		DecimalMeasure distance = DecimalMeasure.exact(Real.valueOf(175572.473), SI.METER); 
		assertEquals(distance, model.distance(c1, c2));
		
		
		 c1 = new Coordinates(
				AngularPosition.degrees(0),
				AngularPosition.degrees(0)
		);
				
		 c2 = new Coordinates(
				AngularPosition.degrees(52),
				AngularPosition.degrees(0)
		);
		
	    distance = DecimalMeasure.exact(Real.valueOf(5763343.550), SI.METER); 
		assertEquals(distance, model.distance(c1, c2));
		
	}
}
