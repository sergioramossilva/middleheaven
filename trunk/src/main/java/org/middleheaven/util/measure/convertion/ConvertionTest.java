package org.middleheaven.util.measure.convertion;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.util.measure.AngularMeasure;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.NonSI;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.measures.Angle;
import org.middleheaven.util.measure.measures.Temperature;


public class ConvertionTest {

	@Test
	public void testTemperatures(){
		
		DecimalMeasure<Temperature> kelvin = DecimalMeasure.exact(Real.valueOf(373.15), SI.KELVIN);
		DecimalMeasure<Temperature> celsius = DecimalMeasure.exact(Real.valueOf(100), NonSI.CELSIUS);
		DecimalMeasure<Temperature> fahrenheit = DecimalMeasure.exact(Real.valueOf(212), NonSI.FAHRENHEIT);
		
		assertEquals(fahrenheit,celsius.convertTo(NonSI.FAHRENHEIT));
		assertEquals(celsius,fahrenheit.convertTo(NonSI.CELSIUS));
		
		assertEquals(celsius,kelvin.convertTo(NonSI.CELSIUS));
		assertEquals(kelvin,celsius.convertTo(SI.KELVIN));
	}
	
	
	@Test
	public void testAngles(){
		
		DecimalMeasure<Angle> radians = DecimalMeasure.exact(Real.valueOf(Math.PI), SI.RADIANS);
		DecimalMeasure<Angle> degrees = DecimalMeasure.exact(Real.valueOf(180), SI.DEGREE);
	
		assertEquals(radians,degrees.convertTo(SI.RADIANS));
		assertEquals(degrees,radians.convertTo(SI.DEGREE));
		
		AngularMeasure aradians = AngularMeasure.radians(Math.PI);
		AngularMeasure adegrees = AngularMeasure.degrees(180);
	
		assertEquals(aradians,adegrees.toRadians());
		assertEquals(adegrees,aradians.toDegrees());
		
		assertEquals(aradians,adegrees.convertTo(SI.RADIANS));
		assertEquals(adegrees,adegrees.convertTo(SI.DEGREE));
	}
}
