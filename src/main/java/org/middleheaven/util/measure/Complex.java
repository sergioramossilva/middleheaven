package org.middleheaven.util.measure;

import org.middleheaven.util.measure.structure.Field;


public abstract class Complex extends Number<Complex> implements Field<Complex> ,Conjugatable<Complex> {

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
	
	public abstract Complex conjugate();
	public abstract Real real();
	public abstract Real imaginary();
	
	public abstract Real magnitude();
	
	public abstract Complex i();
	
	/**
	 * Complex are not ordable. This method compares the magnitude of the complex
	 */
	public abstract int compareTo(Complex other);


}
