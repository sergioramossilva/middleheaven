package org.middleheaven.measures;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Angle;
import org.middleheaven.quantity.measurables.Temperature;
import org.middleheaven.quantity.measure.AngularMeasure;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.NonSI;
import org.middleheaven.quantity.unit.SI;


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
