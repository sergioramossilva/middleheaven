package org.middleheaven.quantity.math;

import org.middleheaven.quantity.measure.AngularMeasure;
import org.middleheaven.quantity.structure.Field;


public abstract class Complex extends Number<Complex> implements Field<Complex> ,Conjugatable<Complex> {

	private static final long serialVersionUID = 5580549238295872790L;
	
	public static Complex polar(Real magnitude, AngularMeasure phase) {
		
		Real real = magnitude.times(phase.cos());
		Real imaginary = magnitude.times(phase.sin());
		return (Complex)NumberFactory.getFactory().numberFor(Complex.class , real ,  imaginary);
	}
	
	public static Complex valueOf(Real real, Real imaginary) {
		return (Complex)NumberFactory.getFactory().numberFor(Complex.class , real ,  imaginary);
	}
	
	public static Complex valueOf(java.lang.Number real, java.lang.Number imaginary) {
		return (Complex)NumberFactory.getFactory().numberFor(Complex.class ,real.toString() + "+i" + imaginary.toString());
	}
	
	public static Complex valueOf(String value) {
		return (Complex)NumberFactory.getFactory().numberFor(Complex.class ,value);
	}
	
	public static Complex ONE(){
		return NumberFactory.getFactory().numberFor(Complex.class , "1+i0");
	}

	public static Complex ZERO(){
		return NumberFactory.getFactory().numberFor(Complex.class , "0+i0");
	}
	
	public static Complex I(){
		return NumberFactory.getFactory().numberFor(Complex.class , "0+i1");
	}
	
	@Override
	public Number<Complex> promote(Number<?> other) {
		return NumberFactory.getFactory().promote(other, Complex.class); 
	}
	
	/**
	 * @return this complex number conjugate. The conjugate of c is denotes c* and is calculated 
	 * from c (c= a + ib) as c* = a - ib
	 */
	public abstract Complex conjugate();
	
	/**
	 * 
	 * @return the real part of this complex
	 */
	public abstract Real real();
	
	/**
	 * 
	 * @return the imaginary part of this complex
	 */
	public abstract Real imaginary();
	
	/**
	 * Returns the magnitude of this complex. The magnitude of c= a +ib is 
	 * |c| = a<sup>2</sup> + b<sup>2</sup> 
	 * @return the magnitude of this complex. 
	 */
	public abstract Real magnitude();
	
	/**
	 * 
	 * @return the imaginary unit. The imaginary unit can be writen as a complex number 
	 * with no real part ( i = 0 + i.1)
	 */
	public abstract Complex i();
	



}
