package org.middleheaven.util.measure;


/**
 * Implements Unit as a pair Dimension , simbol
 * 
 * @author Sergio M.M. Taborda
 *
 */
class BaseUnit extends Unit {


	private Dimension dim;
	
	private String symbol;
	
	
	protected BaseUnit(Dimension dim, String symbol){
		this.dim = dim;
		this.symbol = symbol;
	}
	
	public Dimension dimension() {
		return dim;
	}
	
	public String symbol() {
		return symbol;
	}


	public Unit over(Unit other) {
		return CompositeUnit.over(this, other);
	}

	public Unit times(Unit other) {
		return CompositeUnit.times(this, other);
	}
	
	public Unit minus(Unit other) throws IncompatibleUnitsException {
		return plus(other);
	}

	public Unit plus(Unit other) throws IncompatibleUnitsException {
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


	public boolean equals(Unit other) {
		return  this.symbol.equals(other.symbol()) && this.dim.equals(other.dimension());
	}
	
	public int hashCode(){
		return this.symbol.hashCode();
	}

	public boolean isCompatible(Unit other) {
		return this.dim.equals(other.dimension());
	}

	@Override
	public String toString() {
		return symbol;
	}

	@Override
	public Unit raise(int value) {
		return CompositeUnit.raise (this, value);
	}

	
}
