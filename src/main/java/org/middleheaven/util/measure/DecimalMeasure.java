package org.middleheaven.util.measure;

import java.awt.geom.Area;

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

	public static <T extends Measurable> DecimalMeasure<T> measure(java.lang.Number value,java.lang.Number uncertainty, Unit<T> unit){
		return measure( Real.valueOf(value),Real.valueOf(uncertainty), unit);
	}
	
	public static <T extends Measurable> DecimalMeasure<T> exact(java.lang.Number value, Unit<T> unit){
		return measure( Real.valueOf(value),Real.ZERO(), unit);
	}
	
	public static <T extends Measurable> DecimalMeasure<T> measure(Real value, Real uncertainty,Unit<T> unit){
		return new DecimalMeasure<T>(value,uncertainty,unit);
	}

	public static <T extends Measurable> DecimalMeasure<T> exact(Real value, Unit<T> unit){
		return new DecimalMeasure<T>(value,value.minus(value),unit);
	}
	
	
	protected DecimalMeasure(Real amount, Real uncertainty, Unit unit) {
		super(amount, uncertainty, unit);
	}

	public DecimalMeasure convert(UnitConverter<Real> converter, Unit newUnit) {
		return new DecimalMeasure(converter.convert(amount), converter.convert(uncertainty), newUnit);
	}
	
	public DecimalMeasure negate() {
		return new DecimalMeasure(this.amount.negate(),this.uncertainty, this.unit);
	}
	
	
	public DecimalMeasure over(Real other) {
		return new DecimalMeasure(this.amount.over(other), this.uncertainty.over(other), this.unit);
	}

	public DecimalMeasure times(Real other) {
		return new DecimalMeasure(this.amount.times(other), this.uncertainty.times(other), this.unit);
	}

	public DecimalMeasure  one() {
		return exact(amount.one(),this.unit);
	}

	public DecimalMeasure  zero() {
		return exact(amount.zero(),this.unit);
	}
	
	public <T extends Measurable> DecimalMeasure<T> times(DecimalMeasure<?> other) {
		return new DecimalMeasure<T>(this.amount().times(other.amount()),timesError(other),this.unit.times(other.unit()));
	}
	
	public <T extends Measurable> DecimalMeasure<T> over(DecimalMeasure<?> other) {
		return new DecimalMeasure<T>(this.amount().over(other.amount()),overError(other),this.unit.over(other.unit()));
	}

	public <T extends Measurable > DecimalMeasure<T>  inverse() {
		Unit<T> unit = SI.DIMENTIONLESS.over(this.unit);
		return new DecimalMeasure<T>(this.amount.inverse(), this.uncertainty, unit);
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

	@SuppressWarnings("unchecked")
	public <T extends Measurable > DecimalMeasure<T> cast() {
		return (DecimalMeasure<T>) this;
	}


}
