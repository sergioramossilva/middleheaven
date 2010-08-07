package org.middleheaven.quantity.unit;

import org.middleheaven.quantity.measurables.Measurable;


/**
 * Implements Unit as a pair Dimension , simbol
 * 
 *
 */
class BaseUnit<E extends Measurable> extends Unit<E> {

	private Dimension<E> dim;
	private String symbol;
	
	protected BaseUnit(Dimension<E> dim, String symbol){
		this.dim = dim;
		this.symbol = symbol;
	}
	
	public Dimension<E> dimension() {
		return dim;
	}
	
	public String symbol() {
		return symbol;
	}
	
	public boolean equals(Object other){
		return super.equals(other);
	}
	
	public boolean equals(Unit<?> other) {
		return this.symbol.equals(other.symbol()) && this.dim.equals(other.dimension());
	}
	
	public int hashCode(){
		return this.symbol.hashCode();
	}



	




	
}
