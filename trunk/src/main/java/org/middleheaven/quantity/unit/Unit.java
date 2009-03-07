package org.middleheaven.quantity.unit;

import org.middleheaven.quantity.measurables.Measurable;

public abstract class Unit<E extends Measurable> {


	public static  <M extends Measurable> Unit<M> unit(Dimension<M> dim, String symbol){
		return new BaseUnit<M>(dim,symbol);
	}

	public abstract Dimension<E> dimension();
	public abstract String symbol();

	public abstract Unit<E> plus(Unit<E> other) throws IncompatibleUnitsException;
	public abstract Unit<E> minus(Unit<E> other) throws IncompatibleUnitsException;
	public abstract <T extends Measurable> Unit<T> times(Unit<?> other);
	public abstract <T extends Measurable> Unit<T> over(Unit<?> other);
	
	public abstract boolean equals(Unit<E> other);
	public abstract String toString();
	
	public abstract boolean isCompatible(Unit<?> other);
	public abstract <C extends Measurable> Unit<C>  raise(int exponent);

	public abstract <C extends Measurable> Unit<C> cast();

}
