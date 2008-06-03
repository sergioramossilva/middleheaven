package org.middleheaven.util.measure.physics;

import org.middleheaven.util.measure.Measure;
import org.middleheaven.util.measure.SI;

/**
 * Enumeration of constants used in physics as of 06-2007 TODO 
 * 
 * Reference: <a href="http://physics.nist.gov/cuu/Constants/index.html">CODATA Internationally recommended values of the Fundamental Physical Constants </a>  
 * @author Sergio M.M. Taborda
 *
 */
public interface Constants {

	/**
	 *  Speed of light in vacuum 
	 */
	Measure c = Measure.exact(299792458, SI.METER.over(SI.SECOND));
	
	/**
	 *  Speed of light in vacuum , squared
	 */
	Measure c_square = c.times(c);
	
	/**
	 *  Magnetic constant 
	 */
	Measure miu_zero = Measure.exact(4e-7 * Math.PI, SI.NEWTON.over(SI.AMPERE.raise(2)));
	
	/**
	 *  Newtonian constant of gravitation 
	 */
	Measure G = Measure.measure(6.67428e-11, 1.0e-4, SI.METER.raise(3).over(SI.SECOND.raise(2)).over(SI.KILOGRAM) );
	
	/**
	 *  Electric constant, also known as permittivity of vacuum. (1/(µ0·c²)) 
	 */
	//Measure epslion_zero = miu_zero.times(c_square).inverse();
}
