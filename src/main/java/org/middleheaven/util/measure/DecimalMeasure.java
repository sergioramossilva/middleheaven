package org.middleheaven.util.measure;

import org.middleheaven.util.measure.convertion.Convertable;
import org.middleheaven.util.measure.convertion.UnitConverter;
import org.middleheaven.util.measure.measures.Measurable;
import org.middleheaven.util.measure.structure.Field;

/**
 * A real measure. <code>DecimalMeasure</code> is the generic and default form for explictly
 * handle measures. You may find useful to use <code>AngularMeasure</code> for angular measures
 *
 * @param <E>
 * @see {@link org.middleheaven.util.measure.AngularMeasure}
 */
public class DecimalMeasure<E extends Measurable> extends Measure<E,Real> implements Comparable<DecimalMeasure<E>> , Field<DecimalMeasure<E>> , Convertable<Real,DecimalMeasure<E>> {

	public static <T extends Measurable> DecimalMeasure<T> measure(java.lang.Number value,java.lang.Number uncertainty, Unit unit){
		return measure( Real.valueOf(value),Real.valueOf(uncertainty), unit);
	}
	
	public static <T extends Measurable> DecimalMeasure<T> exact(java.lang.Number value, Unit unit){
		return measure( Real.valueOf(value),Real.ZERO(), unit);
	}
	
	public static  <T extends Measurable> DecimalMeasure<T> measure(Real value, Real uncertainty,Unit unit){
		return new DecimalMeasure<T>(value,uncertainty,unit);
	}

	public static <T extends Measurable> DecimalMeasure<T> exact(Real value, Unit unit){
		return new DecimalMeasure<T>(value,value.minus(value),unit);
	}
	
	
	protected DecimalMeasure(Real amount, Real uncertainty, Unit unit) {
		super(amount, uncertainty, unit);
	}

	public DecimalMeasure<E> convert(UnitConverter<Real> converter, Unit newUnit) {
		return new DecimalMeasure<E>(converter.convert(amount), converter.convert(uncertainty), newUnit);
	}
	
	public DecimalMeasure<E>negate() {
		return new DecimalMeasure<E>(this.amount.negate(),this.uncertainty, this.unit);
	}
	
	
	public DecimalMeasure<E> over(Real other) {
		return new DecimalMeasure<E>(this.amount.over(other), this.uncertainty.over(other), this.unit);
	}

	public DecimalMeasure<E> times(Real other) {
		return new DecimalMeasure<E>(this.amount.times(other), this.uncertainty.times(other), this.unit);
	}

	public DecimalMeasure<E>  one() {
		return exact(amount.one(),this.unit);
	}

	public DecimalMeasure<E>  zero() {
		return exact(amount.zero(),this.unit);
	}
	
	public DecimalMeasure<E> times(DecimalMeasure<E> other) {
		return new DecimalMeasure<E>(this.amount().times(other.amount()),timesError(other),this.unit.times(other.unit()));
	}
	
	public DecimalMeasure<E> over(DecimalMeasure<E> other) {
		return new DecimalMeasure<E>(this.amount().over(other.amount()),overError(other),this.unit.over(other.unit()));
	}

	public DecimalMeasure<E>  inverse() {
		return new DecimalMeasure<E>(this.amount.inverse(), this.uncertainty, SI.DIMENTIONLESS.over(this.unit));
	}
	

	public DecimalMeasure<E> plus(DecimalMeasure<E> other) throws IncompatibleUnitsException {
		assertCompatible (other);
		return new DecimalMeasure<E>(other.amount().plus(this.amount), this.uncertainty.plus(other.uncertainty) , this.unit.plus(other.unit()));
	}
	
	public DecimalMeasure<E> minus(DecimalMeasure<E> other) throws IncompatibleUnitsException {
		assertCompatible (other);
		return new DecimalMeasure<E>(other.amount().minus(this.amount),this.uncertainty.plus(other.uncertainty),this.unit.minus(other.unit()));
	}

	@Override
	public int compareTo(DecimalMeasure<E> other) {
		assertCompatible (other);
		return this.amount.compareTo(other.amount);
	}


}
