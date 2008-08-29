package org.middleheaven.util.measure.impl;

import java.math.BigDecimal;

import org.middleheaven.util.measure.Complex;
import org.middleheaven.util.measure.Real;

public class BigComplex extends Complex {

    static final Complex ONE = new BigComplex(Real.ONE(),Real.ZERO());
	static final Complex ZERO = new BigComplex(Real.ZERO(),Real.ZERO());
    static final Complex I = new BigComplex(Real.ZERO(),Real.ONE());

	private Real real;
	private Real imaginary;

	public BigComplex(Real real, Real imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public BigComplex(String value) {
		if (value.indexOf("+i")>0){
			this.real = Real.valueOf(value.substring(0,value.indexOf("+i")));
			this.imaginary = Real.valueOf(value.substring(value.indexOf("+i")+2));
		} else {
			this.real = Real.valueOf(value);
			this.imaginary = Real.ZERO();
		}
	}

	@Override
	public BigDecimal asNumber() {
		return this.magnitude().asNumber();
	}

	@Override
	public boolean isZero() {
		return real.isZero() && imaginary.isZero();
	}

	@Override
	protected int rank() {
		return 1;
	}

	@Override
	public String toString() {
		return this.real.toString() + "+i" + this.imaginary.toString();
	}

	@Override
	public Complex one() {
		return ONE;
	}

	@Override
	public Complex zero() {
		return ZERO;
	}

	@Override
	public Complex i() {
		return I;
	}

	@Override
	public Real magnitude() {
		return this.real.times(this.real).plus(this.imaginary.times(this.imaginary)).sqrt();
	}
	
	private Real magnitudeSquare(){
		return this.real.times(this.real).plus(this.imaginary.times(this.imaginary));
	}
	@Override
	public Complex times(Complex other) {
		// (a+ib) * (c+di) = (ac-db) + (bc+ad)i
		return  new BigComplex(
				this.real.times(other.real()).minus(other.imaginary().times(this.imaginary)) , 
				this.imaginary.times(other.real()).plus(this.real.times(other.imaginary()))
		);
	}
	
	@Override
	public Complex over(Complex other) {
		// (a+ib)/(c+id) = (a+ib) * (c+id)^-1  
		return this.times(other.inverse());
	}

	@Override
	public Complex inverse() {
		// a+ib ->  a/m - bi/m  , m = a*a + b*b
		Real magnitude2 = this.magnitudeSquare();
		if (magnitude2.isZero()){
			throw new ArithmeticException("Inverse of " + this.toString() + " is not defined");
		}
		return  new BigComplex(
				this.real.over(magnitude2), 
				this.imaginary.over(magnitude2)
		);
	}

	
	@Override
	public Complex negate() {
		return new BigComplex(this.real.negate(), this.imaginary.negate());
	}

	@Override
	public Complex plus(Complex other) {
		return  new BigComplex(this.real.plus(other.real()) , this.imaginary.plus(other.imaginary()));
	}

	@Override
	public Complex minus(Complex other) {
		return  new BigComplex(this.real.minus(other.real()) , this.imaginary.minus(other.imaginary()));
	}
	
	@Override
	public int compareTo(Complex other) {
		return this.magnitude().compareTo(other.magnitude());
	}

	@Override
	public Complex conjugate() {
		return new BigComplex(this.real, this.imaginary.negate());
	}

	@Override
	public Real imaginary() {
		return imaginary;
	}

	@Override
	public Real real() {
		return real;
	}

	


}
