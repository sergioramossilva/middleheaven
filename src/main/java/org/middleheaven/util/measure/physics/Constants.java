package org.middleheaven.util.measure.physics;

import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.measures.Velocity;

/**
 * Enumeration of constants used in physics as of 06-2007 
 * 
 * TODO : complete list 
 * 
 * @see <a href="http://physics.nist.gov/cuu/Constants/index.html">CODATA Internationally recommended values of the Fundamental Physical Constants </a>  
 *
 *
 */
public interface Constants {

	/**
	 *  Speed of light in vacuum 
	 */
	DecimalMeasure<Velocity> c = DecimalMeasure.exact(299792458, SI.METER.over(SI.SECOND)).cast();
	
	/**
	 *  Speed of light in vacuum , squared
	 */
	DecimalMeasure<Velocity> c_square = c.times(c);
	
	/**
	 *  Magnetic constant 
	 */
	DecimalMeasure<?> miu_zero = DecimalMeasure.exact(4e-7 * Math.PI, SI.NEWTON.over(SI.AMPERE.raise(2)));
	
	/**
	 *  Newtonian constant of gravitation 
	 */
	DecimalMeasure<?> G = DecimalMeasure.measure(6.67428e-11, 1.0e-4, SI.METER.raise(3).over(SI.SECOND.raise(2)).over(SI.KILOGRAM) );
	
	/**
	 *  Electric constant, also known as permitivity of vacuum. (1/(µ0·c²)) 
	 */
	DecimalMeasure<?> epslion_zero = miu_zero.times(c_square).inverse();
}
