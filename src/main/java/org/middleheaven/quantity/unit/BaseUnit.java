package org.middleheaven.quantity.unit;

import org.middleheaven.quantity.measurables.Measurable;


/**
 * Implements Unit as a pair Dimension , simbol
 * 
 * @author Sergio M.M. Taborda
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


	public <T extends Measurable> Unit<T> over(Unit<?> other) {
		return CompositeUnit.over(this, other);
	}

	public <T extends Measurable> Unit<T> times(Unit<?> other) {
		return CompositeUnit.times(this, other);
	}
	
	public Unit<E> minus(Unit<E> other) throws IncompatibleUnitsException {
		return plus(other);
	}

	public Unit<E> plus(Unit<E> other) throws IncompatibleUnitsException {
		if (this.isCompatible(other)){
			if (other.equals(this)){
				return this;
			} else {
				// TODO convert
				return this;
			}
		}
		throw new IncompatibleUnitsException(this,other);
	}

	public boolean equals(Unit<E> other) {
		return  this.symbol.equals(other.symbol()) && this.dim.equals(other.dimension());
	}
	
	public int hashCode(){
		return this.symbol.hashCode();
	}

	public boolean isCompatible(Unit<?> other) {
		return this.dim.equals(other.dimension());
	}

	@Override
	public String toString() {
		return symbol;
	}

	@Override
	public  <C extends Measurable> Unit<C> raise(int exponent) {
		return CompositeUnit.raise (this, exponent);
	}
	

	@Override
	public <C extends Measurable> Unit<C> cast() {
		return (Unit<C>) this;
	}





	
}
