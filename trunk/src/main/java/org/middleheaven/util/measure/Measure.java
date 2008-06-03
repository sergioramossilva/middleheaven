package org.middleheaven.util.measure;

import org.middleheaven.util.measure.structure.Field;

public class Measure<F extends Field<F>> implements Quantity {


	public static Measure<Real> measure(java.lang.Number value,java.lang.Number uncertainty, Unit unit){
		return measure( Real.valueOf(value),Real.valueOf(uncertainty), unit);
	}
	
	public static Measure<Real> exact(java.lang.Number value, Unit unit){
		return measure( Real.valueOf(value),Real.ZERO(), unit);
	}
	
	public static <T extends Field<T>> Measure<T> measure(T value, T uncertainty,Unit unit){
		return new Measure<T>(value,uncertainty,unit);
	}

	public static <T extends Field<T>> Measure<T> exact(T value, Unit unit){
		return new Measure<T>(value,value.minus(value),unit);
	}
	
	
	protected F uncertainty;
	
	protected F amount;
	
	protected Unit unit;


	protected Measure(F amount, F uncertainty, Unit unit){
		
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
	
	public F uncertainty() {
		return uncertainty;
	}
	
	public boolean isExact(){
		return uncertainty.plus(uncertainty).equals(uncertainty); //.isZero();
	}
	
	public Unit  unit() {
		return unit;
	}
	
	public String toString(){
		return '(' + this.amount.toString() + ' ' + '\u00B1' + ' ' + this.uncertainty.toString()  + ") " + this.unit.toString();
	}
	

	@Override
	public boolean equals(Object other) {
		return other instanceof Measure && equals ((Measure)other);
	}
	
	public boolean equals(Measure<F> other) {
		return this.amount.equals(other.amount) && this.uncertainty.equals(other.uncertainty) && this.unit.equals(other.unit);
	}
	

	public Measure<F> plus(Measure<F> other) throws IncompatibleUnitsException {
		assertCompatible (other);
		return new Measure<F>(other.amount().plus(this.amount), this.uncertainty.plus(other.uncertainty) , this.unit.plus(other.unit()));
	}
	
	public Measure<F> minus(Measure<F> other) throws IncompatibleUnitsException {
		assertCompatible (other);
		return new Measure<F>(other.amount().minus(this.amount),this.uncertainty.plus(other.uncertainty),this.unit.minus(other.unit()));
	}

	private void assertCompatible(Measure<?> other) {
		if (!this.unit().isCompatible(other.unit())){
			throw new IncompatibleUnitsException(this.unit(),other.unit());
		}
	}

	public Measure<F> negate() {
		return new Measure<F>(this.amount.negate(),this.uncertainty, this.unit);
	}
	

	public Measure<F> times(Measure<F> other) {
		// deltaZ = (deltaX/X + deltaY/Y)*Z = deltaX* Y + deltaY*X
		F error =  this.uncertainty.times(other.amount).plus(other.uncertainty.times(this.amount));
		return new Measure<F>(this.amount().times(other.amount()),error,this.unit.times(other.unit()));
	}
	
	public Measure<F> over(Measure<F> other) {
		// deltaZ = (deltaX/X + deltaY/Y)*Z = deltaX + deltaY.X/Y 
		F error =  this.uncertainty.plus(other.uncertainty.times(this.amount().over(other.amount())));
		return new Measure<F>(this.amount().over(other.amount()),error,this.unit.over(other.unit()));
	}

	public Measure<F>  inverse() {
		return new Measure<F>(this.amount.inverse(), this.uncertainty, SI.DIMENTIONLESS.over(this.unit));
	}

	public F amount() {
		return amount;
	}


	public Measure<F> over(F other) {
		return new Measure(this.amount.over(other), this.uncertainty.over(other), this.unit);
	}


	public Measure<F> times(F other) {
		return new Measure(this.amount.times(other), this.uncertainty.times(other), this.unit);
	}

}
