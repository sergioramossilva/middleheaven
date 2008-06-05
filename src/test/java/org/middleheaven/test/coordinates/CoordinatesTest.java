package org.middleheaven.test.coordinates;

import org.junit.Test;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.coordinate.Coordinate;
import org.middleheaven.util.measure.coordinate.GeographicReferenceSystem;


public class CoordinatesTest {

	
	@Test
	public void testCoordinates(){
		
		Coordinate cor = new GeographicReferenceSystem().coordinateAt(
				DecimalMeasure.exact(1, SI.METER),
				DecimalMeasure.exact(1, SI.METER),
				DecimalMeasure.exact(1, SI.METER)
		);
	}
}
