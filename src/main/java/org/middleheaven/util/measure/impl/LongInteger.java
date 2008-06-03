package org.middleheaven.util.measure.impl;

import java.math.BigDecimal;

import org.middleheaven.util.Incrementable;
import org.middleheaven.util.measure.Integer;
import org.middleheaven.util.measure.Number;

public class LongInteger extends Integer {


	private static final long serialVersionUID = 7891405602158515389L;
	
    private long value = 0;
	
	
	public LongInteger(String value) {
		this.value = Long.parseLong(value);
	}
	
    LongInteger(long value) {
    	this.value = value;
	}

	@Override
	public Integer next() {
		return new LongInteger(value+1);
	}

	@Override
	public Integer previous() {
		return new LongInteger(value-1);
	}
	
	@Override
	public boolean isZero() {
		return value ==0;
	}

	@Override
	public BigDecimal asNumber() {
		return new BigDecimal(value);
	}

	@Override
	public Integer inverse() {
		return new LongInteger(1/value);
	}

	@Override
	public Integer negate() {
		return new LongInteger(-value);
	}

	@Override
	public Integer plus(Integer other) {
		if (other instanceof LongInteger){
			return new LongInteger(this.value + ((LongInteger)other).value);
		} else {
			return other.plus(this);
		}
	}

	@Override
	public Integer times(Integer other) {
		if (other instanceof LongInteger){
			return new LongInteger(this.value * ((LongInteger)other).value);
		} else {
			return other.plus(this);
		}
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}


	
	@Override
	public Integer over(Integer other) {
		return new LongInteger(this.value/other.asNumber().longValue());
	}

	
	@Override
	public int compareTo(Integer other) {
		if (other instanceof LongInteger){
			return (int)(this.value - ((LongInteger)other).value);
		}
		return this.asNumber().compareTo(other.asNumber());
	}

	@Override
	public <T extends Incrementable<Integer>> T incrementBy(Integer increment) {
		return (T)this.plus(increment);
	}

	@Override
	public boolean isEven() {
		return this.value % 2 == 0 ;
	}


	@Override
	public Integer one() {
	    return new LongInteger(1);
	}

	@Override
	public Integer zero() {
		return new LongInteger(0);
	}

	



}
