package org.middleheaven.quantity.math.impl;

import java.math.BigDecimal;

import org.middleheaven.quantity.math.BigInt;
import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.Real;

public class RealPairComplex extends Complex {

    private static final Complex ONE = new RealPairComplex(Real.ONE(),Real.ZERO());
    private static final Complex ZERO = new RealPairComplex(Real.ZERO(),Real.ZERO());
    private static final Complex I = new RealPairComplex(Real.ZERO(),Real.ONE());

	private Real real;
	private Real imaginary;

	public RealPairComplex(Real real, Real imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public RealPairComplex(String value) {
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
		return  new RealPairComplex(
				this.real.times(other.toReal()).minus(other.toImaginary().times(this.imaginary)) , 
				this.imaginary.times(other.toReal()).plus(this.real.times(other.toImaginary()))
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
		
		final Real r = this.real.over(magnitude2);
		final Real i = this.imaginary.over(magnitude2);
		
		return new RealPairComplex(r, i);
	}

	
	@Override
	public Complex negate() {
		return new RealPairComplex(this.real.negate(), this.imaginary.negate());
	}

	@Override
	public Complex plus(Complex other) {
		return  new RealPairComplex(this.real.plus(other.toReal()) , this.imaginary.plus(other.toImaginary()));
	}

	@Override
	public Complex minus(Complex other) {
		return  new RealPairComplex(this.real.minus(other.toReal()) , this.imaginary.minus(other.toImaginary()));
	}

	@Override
	public Complex conjugate() {
		return new RealPairComplex(this.real, this.imaginary.negate());
	}

	@Override
	public Real toImaginary() {
		return imaginary;
	}

	@Override
	public Real toReal() {
		return real;
	}

	@Override
	public Complex minus(Number n) {
		return this.minus(new RealPairComplex(Real.valueOf(n), Real.ZERO()));
	}

	@Override
	public Complex over(Number n) {
		return this.over(new RealPairComplex(Real.valueOf(n), Real.ZERO()));
	}

	@Override
	public Complex plus(Number n) {
		return this.plus(new RealPairComplex(Real.valueOf(n), Real.ZERO()));
	}

	@Override
	public Complex times(Number n) {
		return this.times(new RealPairComplex(Real.valueOf(n), Real.ZERO()));
	}



	protected boolean equals(RealPairComplex other){
		return  this.real.equals(other.real) && this.imaginary.equals(other.imaginary);
	}

	@Override
	protected boolean equalsSame(Complex other) {
		return equals( (RealPairComplex) other);
	}

	@Override
	public BigInt toBigInt() {
		return toReal().toBigInt();
	}

	@Override
	public Complex toComplex() {
		return this;
	}




}
