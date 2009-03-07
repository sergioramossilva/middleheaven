package org.middleheaven.quantity.time;

import org.middleheaven.quantity.math.Integer;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;

public class Period extends ElapsedTime implements Comparable<Period>{

	
	public static Period miliseconds (long miliseconds){
		return new Period(miliseconds);
	}
	
	public static Period seconds (long seconds){
		return new Period(seconds*1000);
	}
	
    long miliseconds;
	public Period(long miliseconds) {
		this.miliseconds = miliseconds;
	}


	public Period negate() {
		return new Period(-1 * this.miliseconds);
	}
	
	public long milliseconds(){
		return this.miliseconds;
	}

	public Period plus(Period other) throws IncompatibleUnitsException {
		return new Period (this.miliseconds + other.miliseconds);
	}
	
	public Duration plus(Duration other) throws IncompatibleUnitsException {
		return other.plus(this);
	}

	public Unit unit() {
		return SI.MILISECOND;
	}

	@Override
	public Object clone() {
		return new Period(this.miliseconds);
	}

	public Integer amount() {
		return Integer.valueOf(this.miliseconds);
	}

	public ElapsedTime over(org.middleheaven.quantity.math.Number other) {
		return new Period ( (long)(this.miliseconds / other.asNumber().doubleValue()) );
	}

	public ElapsedTime times(org.middleheaven.quantity.math.Number other) {
		return new Period ( (long)(this.miliseconds * other.asNumber().doubleValue()) );
	}

	public String toString(){
		return Long.toString(this.miliseconds);
	}

	@Override
	public int compareTo(Period other) {
		return (int)(this.miliseconds - other.miliseconds);
	}
}
