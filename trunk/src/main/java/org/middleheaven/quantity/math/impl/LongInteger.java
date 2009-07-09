package org.middleheaven.quantity.math.impl;

import java.math.BigDecimal;

import org.middleheaven.quantity.math.Integer;
import org.middleheaven.quantity.math.Numeral;
import org.middleheaven.util.Incrementable;

public class LongInteger extends Integer {


	private static final long serialVersionUID = 7891405602158515389L;
	
	private static final LongInteger ONE = new LongInteger(1);
	private static final LongInteger ZERO = new LongInteger(0);
	
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

	@SuppressWarnings("unchecked")
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
	    return ONE;
	}

	@Override
	public Integer zero() {
		return ZERO;
	}

	@Override
	public Integer minus(java.lang.Number n) {
		return this.minus(new LongInteger(n.longValue()));
	}

	@Override
	public Integer over(java.lang.Number n) {
		return this.over(new LongInteger(n.longValue()));
	}

	@Override
	public Integer plus(java.lang.Number n) {
		return this.plus(new LongInteger(n.longValue()));
	}

	@Override
	public Integer times(java.lang.Number n) {
		return this.times(new LongInteger(n.longValue()));
	}

	/*
	public boolean equals(Object other){
		return other instanceof LongInteger && equals((LongInteger)other);
	}
	*/

	public boolean equals(LongInteger other){
		return  this.value == other.value;
	}

	@Override
	protected boolean equalsSame(Integer other) {
		return equals( (LongInteger) other);
	}

	@Override
	public int compareTo(Numeral<? super Integer> o) {
		return this.asNumber().compareTo(o.asNumber());
	}



}
