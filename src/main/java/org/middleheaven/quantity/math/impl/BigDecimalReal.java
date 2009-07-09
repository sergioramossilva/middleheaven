package org.middleheaven.quantity.math.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.util.Hash;
import org.middleheaven.util.Incrementable;

public class BigDecimalReal extends Real{

	private static final long serialVersionUID = 1L;

	// Ratio pattern like implementation to adjourn the 
	// division as much as possible.
	private BigDecimal numerator;
	private BigDecimal denominator;

	BigDecimalReal (String value){
		this(new BigDecimal(value));
	}

	BigDecimalReal (long value){
		this(new BigDecimal(value));
	}

	BigDecimalReal (BigDecimal value){
		this( value , BigDecimal.ONE);
	}

	BigDecimalReal (BigDecimal numerator,BigDecimal denominator){
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public BigDecimal asNumber() {
		return numerator.signum()==0 
					? BigDecimal.ZERO 
					: denominator.intValue()==1 
							? numerator 
							: numerator.divide(denominator, 15, RoundingMode.HALF_EVEN);
	}


	@Override
	public boolean isZero() {
		return numerator.signum()==0;
	}

	@Override
	public Real inverse() {
		return new BigDecimalReal(denominator, numerator);
	}

	@Override
	public Real negate() {
		if (denominator.signum()>0){
			// negate numerator
			return new BigDecimalReal(numerator.negate(), denominator);
		} else {
			// negate denominator
			return new BigDecimalReal(numerator, denominator.negate());
		}
	}

	@Override
	public Real plus(Real other) {
		if (other instanceof BigDecimalReal){
			return plus (((BigDecimalReal)other).numerator , ((BigDecimalReal)other).denominator);
		} else {
			return plus (new BigDecimal(other.toString()), BigDecimal.ONE);
		}
	}

	private BigDecimalReal plus(BigDecimal otherNumerator, BigDecimal otherDenominator){
		BigDecimal[] multipliers = this.multipliers(this.denominator,otherDenominator );

		return new BigDecimalReal(
				this.numerator.multiply(multipliers[0]).add(otherNumerator.multiply(multipliers[1])),
				this.denominator.multiply(multipliers[0])
		).simplify();
	}

	private BigDecimalReal simplify() {

		if (denominator.equals(BigDecimal.ONE) || numerator.signum()==0){
			return this; // is zero or divided by 1
		}
		BigDecimal min = denominator.min(numerator);

		while (numerator.remainder(min).signum()!=0 &&  denominator.remainder(min).signum()!=0){
			min = min.subtract(BigDecimal.ONE);
		}

		if (min.equals(BigDecimal.ONE)){
			// already simplified
			return this;
		} else {
			try{
				return new BigDecimalReal(
						numerator.divide(min),
						denominator.divide(min)
				);
			} catch (ArithmeticException e){
				return this;
			}
		}
	}

	private BigDecimal[] multipliers (BigDecimal d1 , BigDecimal d2){
		int compare = d1.compareTo(d2);
		BigDecimal[] division;
		if ( compare==0){
			return new BigDecimal[]{BigDecimal.ONE,BigDecimal.ONE};
		} else if ( compare <0 ){
			division = d2.divideAndRemainder(d1);
			if (division[1].signum()==0){
				return new BigDecimal[]{division[0],BigDecimal.ONE};
			} 
		} else {
			division = d1.divideAndRemainder(d2);
			if (division[1].signum()==0){
				return new BigDecimal[]{BigDecimal.ONE,division[0]};
			} 
		}
		return new BigDecimal[]{d2,d1};
	}


	@Override
	public Real times(Real other) {
		if (other instanceof BigDecimalReal){
			return times(((BigDecimalReal)other).numerator, ((BigDecimalReal)other).denominator);
		} else {
			return times(new BigDecimal(other.toString()) , BigDecimal.ONE);
		}
	}

	private BigDecimalReal times(BigDecimal otherNumerator, BigDecimal otherDenominator){
		return new BigDecimalReal(
				this.numerator.multiply(otherNumerator),
				this.denominator.multiply(otherDenominator)
		).simplify();
	}

	@Override
	public Real over(Real other) {
		// times the inverse
		if (other instanceof BigDecimalReal){
			if (((BigDecimalReal)other).numerator.signum()==0){
				throw new ArithmeticException("Division by zero");
			}
			return times( ((BigDecimalReal)other).denominator,((BigDecimalReal)other).numerator);
		} else {
			BigDecimal value = new BigDecimal(other.toString());
			if (value.signum()==0){
				throw new ArithmeticException("Division by zero");
			}
			return times( BigDecimal.ONE , value);
		}
	}


	@Override
	public String toString() {
		return this.asNumber().toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Incrementable<Real>> T incrementBy(Real increment) {
		return (T)this.plus(increment);
	}


	@Override
	public Real one() {
		return new BigDecimalReal(BigDecimal.ONE);
	}

	@Override
	public Real zero() {
		return new BigDecimalReal(BigDecimal.ZERO);
	}

	@Override
	public Real sqrt() {
		return new BigDecimalReal(new BigDecimal(Math.sqrt(this.numerator.doubleValue())), new BigDecimal(Math.sqrt(this.denominator.doubleValue()))).simplify();
	}

	@Override
	public Real minus(Number n) {
		return this.minus(Real.valueOf(n));
	}

	@Override
	public Real over(Number n) {
		return this.over(Real.valueOf(n));
	}

	@Override
	public Real plus(Number n) {
		return this.plus(Real.valueOf(n));
	}

	@Override
	public Real times(Number n) {
		return this.times(Real.valueOf(n));
	}

	public boolean equals(Object other){
		return other instanceof BigDecimalReal && equals((BigDecimalReal)other);
	}

	public boolean equals(BigDecimalReal other){
		return (this.denominator.compareTo(other.denominator)==0 &&  this.numerator.compareTo(other.numerator) ==0)
		|| this.numerator.multiply(other.denominator).compareTo(this.denominator.multiply(other.numerator))==0;
	}

	@Override
	protected boolean equalsSame(Real other) {
		return equals((BigDecimalReal)other);
	}
	
	public int hashCode(){
		return Hash.hash(numerator).hash(denominator).hashCode();
	}

	@Override
	public int compareTo(org.middleheaven.quantity.math.Numeral<? super Real> o) {
		return this.asNumber().compareTo(o.asNumber());
	}


}
