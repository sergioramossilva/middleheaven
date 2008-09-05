package org.middleheaven.util.measure.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.middleheaven.util.Incrementable;
import org.middleheaven.util.measure.Real;

public class BigDecimalReal extends Real{


	private static final long serialVersionUID = 1L;
	
	private BigDecimal value;
	
    BigDecimalReal (String value){
		this(new BigDecimal(value));
	}
	
    BigDecimalReal (long value){
		this(new BigDecimal(value));
	}
    
    BigDecimalReal (BigDecimal value){
		this.value = value;
	}
    
	public BigDecimal asNumber() {
		return value;
	}


	@Override
	public boolean isZero() {
		return value.signum()==0;
	}

	@Override
	public Real inverse() {
		return new BigDecimalReal(BigDecimal.ONE.divide(value,19,RoundingMode.HALF_EVEN));
	}

	@Override
	public Real negate() {
		return new BigDecimalReal(value.negate());
	}

	@Override
	public Real plus(Real other) {
		if (other instanceof BigDecimalReal){
			return new BigDecimalReal(this.value.add(((BigDecimalReal)other).value));
		} else {
			return new BigDecimalReal(this.value.add(new BigDecimal(other.toString())));
		}
	}

	
	@Override
	public Real times(Real other) {
		if (other instanceof BigDecimalReal){
			return new BigDecimalReal(this.value.multiply(((BigDecimalReal)other).value));
		} else {
			return new BigDecimalReal(this.value.multiply(new BigDecimal(other.toString())));
		}
	}

	@Override
	public Real over(Real other) {
		if (other instanceof BigDecimalReal){
			return new BigDecimalReal(this.value.divide(((BigDecimalReal)other).value , 19, RoundingMode.HALF_EVEN));
		} else {
			return new BigDecimalReal(this.value.divide(new BigDecimal(other.toString())));
		}
	}
	
	
	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public int compareTo(Real o) {
		return this.value.compareTo(o.asNumber());
	}

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
		return new BigDecimalReal(new BigDecimal(Math.sqrt(this.value.doubleValue())));
	}






}
