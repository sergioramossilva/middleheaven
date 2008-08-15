package org.middleheaven.util.measure;

import org.middleheaven.util.measure.measures.Measurable;
import org.middleheaven.util.measure.structure.Field;

/**
 * The value of a measurement and the associated uncertainty
 *
 * @param <E> the associated <code>Measurable</code>
 * @param <F> the associated underlying <code>Field</code> , normally <code>Real</code>
 */
public abstract class Measure<E extends Measurable , F extends Field<F>> implements Quantity {


	protected F uncertainty;
	protected F amount;
	protected Unit<E> unit;


	protected Measure(F amount, F uncertainty, Unit<E> unit){
		
		if (amount == null ){
			throw new IllegalArgumentException("Amount is required");
		}
		if (uncertainty == null ){
			throw new IllegalArgumentException("Uncertainty is required");
		}
		if (unit == null ){
			throw new IllegalArgumentException("Unit is required");
		}
		this.amount = amount;
		this.unit = unit;
		this.uncertainty = uncertainty;
	}
	
	public final F uncertainty() {
		return uncertainty;
	}
	
	public final boolean isExact(){
		return uncertainty.plus(uncertainty).equals(uncertainty); //.isZero();
	}
	
	public final Unit<E>  unit() {
		return unit;
	}
	
	public final String toString(){
		return '(' + this.amount.toString() + ' ' + '\u00B1' + ' ' + this.uncertainty.toString()  + ") " + this.unit.toString();
	}
	

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Measure)){
			return false;
		}
		Measure m = (Measure)other;
		return this.amount.equals(m.amount) && this.uncertainty.equals(m.uncertainty) && this.unit.equals(m.unit);
	}
	
	protected void assertCompatible(Measure<?,?> other) {
		if (!this.unit().isCompatible(other.unit())){
			throw new IncompatibleUnitsException(this.unit(),other.unit());
		}
	}

	protected F timesError (Measure<?,F> other){
		// deltaZ = (deltaX/X + deltaY/Y)*Z = deltaX* Y + deltaY*X
		return this.uncertainty.times(other.amount).plus(other.uncertainty.times(this.amount));
	}
	
	protected F overError (Measure<?,F> other){
		// deltaZ = (deltaX/X + deltaY/Y)*Z = deltaX + deltaY.X/Y 
		return this.uncertainty.plus(other.uncertainty.times(this.amount().over(other.amount())));
	}

	public final F amount() {
		return amount;
	}




}
