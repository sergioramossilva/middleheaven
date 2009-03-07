package org.middleheaven.quantity.physics;

import org.middleheaven.quantity.measurables.Velocity;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.SI;

/**
 * Enumeration of constants used in physics as of 06-2007 
 * 
 * TODO : complete list 
 * 
 * @see <a href="http://physics.nist.gov/cuu/Constants/index.html">CODATA Internationally recommended values of the Fundamental Physical Constants </a>  
 *
 * The case and letters are selected according to conventions.
 */
public class Constants {

	
	private Constants(){} // utility class, not instanciable
	
	/**
	 *  Speed of light in vacuum 
	 */
	public static final DecimalMeasure<Velocity> c = DecimalMeasure.exact(299792458, SI.METER.over(SI.SECOND)).cast();
	
	/**
	 *  Speed of light in vacuum , squared
	 */
	public static final	DecimalMeasure<Velocity> c_square = c.times(c);
	
	/**
	 *  Magnetic constant 
	 */
	public static final	DecimalMeasure<?> miu_zero = DecimalMeasure.exact(4e-7 * Math.PI, SI.NEWTON.over(SI.AMPERE.raise(2)));
	
	/**
	 *  Newtonian constant of gravitation 
	 */
	public static final	DecimalMeasure<?> G = DecimalMeasure.measure(6.67428e-11, 1.0e-4, SI.METER.raise(3).over(SI.SECOND.raise(2)).over(SI.KILOGRAM) );
	
	/**
	 *  Electric constant, also known as permitivity of vacuum. (1/(µ0·c²)) 
	 */
	public static final	DecimalMeasure<?> epslion_zero = miu_zero.times(c_square).inverse();
}
