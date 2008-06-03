/**
 * 
 */
package org.middleheaven.util.measure;

class UnitPower implements Comparable<UnitPower>{



	Unit unit;
	
	int exponent;
	
	public UnitPower(Unit unit, int exponent){
		this.unit = unit;
		this.exponent = exponent;
	}
	
	public int exponent() {
		return exponent;
	}

	public Unit unit() {
		return unit;
	}

	public String toString(){
		StringBuilder builder = new StringBuilder().append(this.unit);
		if (this.exponent!=1){
			builder.append('^').append(exponent);
		}
		return builder.toString();
	}

	public int compareTo(UnitPower other) {
		return this.unit.symbol().compareTo(other.unit.symbol());
	}
}