package org.middleheaven.util.measure;

public abstract class Unit {


	public static Unit unit(Dimension dim, String symbol){
		return new BaseUnit(dim,symbol);
	}

	public abstract Dimension dimension();
	public abstract String symbol();
	
	public abstract Unit plus(Unit other) throws IncompatibleUnitsException;
	public abstract Unit minus(Unit other) throws IncompatibleUnitsException;
	public abstract Unit times(Unit other);
	public abstract Unit over(Unit other);
	
	public abstract boolean equals(Unit other);
	public abstract String toString();
	
	public abstract boolean isCompatible(Unit other);
	public abstract Unit raise(int value);
	
	
}
