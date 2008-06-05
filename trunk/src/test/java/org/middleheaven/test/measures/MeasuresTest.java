package org.middleheaven.test.measures;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.util.measure.AngularPosition;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.convertion.UnitConversion;


public class MeasuresTest {

	
	@Test
	public void testAngularPosition(){
		
		AngularPosition ap = AngularPosition.degrees(180);
		AngularPosition apc = UnitConversion.convert(ap, SI.RADIANS);
		
		assertEquals(AngularPosition.radians(Math.PI), apc );
		
		
		AngularPosition diff = AngularPosition.degrees(360*2.25);
		
		assertEquals(AngularPosition.degrees(270), ap.plus(diff).reduce());
		
		assertEquals(AngularPosition.degrees(90), ap.plus(diff.negate()).reduce());
	}
}
