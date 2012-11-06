package org.middleheaven.quantity.measure;

import org.middleheaven.quantity.Quantity;
import org.middleheaven.quantity.math.structure.FieldElement;
import org.middleheaven.quantity.unit.IncompatibleUnitsException;
import org.middleheaven.quantity.unit.Unit;

/**
 * The value of a measurement and the associated uncertainty
 *
 * @param <E> the associated <code>Measurable</code>
 * @param <F> the associated underlying <code>Field</code> , normally <code>Real</code>
 */
public abstract class Measure<E extends Measurable , F extends FieldElement<F>> implements Quantity<E>  {


	protected F uncertainty;
	protected F amount;
	protected Unit<E> unit;

	/**
	 * 
	 * Constructor.
	 * @param amount the amount of the unit measured
	 * @param uncertainty the error associated with the measeure
	 * @param unit the unit used in the measure
	 */
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
	
	/**
	 * 
	 * @return the amount of uncertainty.
	 */
	public final F uncertainty() {
		return uncertainty;
	}
	
	/**
	 * 
	 * @return true is this value is exact (no uncertainty)
	 */
	public final boolean isExact(){
		return uncertainty.plus(uncertainty).equals(uncertainty); //.isZero();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public final Unit<E>  unit() {
		return unit;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final String toString(){
		return '(' + this.amount.toString() + ' ' + '\u00B1' + ' ' + this.uncertainty.toString()  + ") " + this.unit.toString();
	}
	

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Measure)){
			return false;
		}
		Measure m = (Measure)other;
		return this.amount.equals(m.amount) && this.uncertainty.equals(m.uncertainty) && this.unit.equals(m.unit);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(){
		return this.amount.hashCode();
	}
	
	/**
	 * 
	 * @param other
	 */
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
