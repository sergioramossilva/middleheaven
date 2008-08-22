package org.middleheaven.util.measure;

import org.middleheaven.util.measure.convertion.Convertable;
import org.middleheaven.util.measure.convertion.UnitConversion;
import org.middleheaven.util.measure.measures.Measurable;

/**
 * A real measure. <code>DecimalMeasure</code> is the generic and default form for explictly
 * handle measures. You may find useful to use <code>AngularMeasure</code> for angular measures
 *
 * @param <E>
 * @see {@link org.middleheaven.util.measure.AngularMeasure}
 */
public class DecimalMeasure<E extends Measurable> extends Measure<E,Real> implements Comparable<DecimalMeasure<E>> , Convertable<E,DecimalMeasure<E>> , Scalable<E,DecimalMeasure<E>> {

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
	
	protected DecimalMeasure(Real amount, Real uncertainty, Unit<E> unit) {
		super(amount, uncertainty, unit);
	}


	public DecimalMeasure<E> negate() {
		return new DecimalMeasure<E>(this.amount.negate(),this.uncertainty, this.unit);
	}
	
	public DecimalMeasure<E> over(Real other) {
		return new DecimalMeasure<E>(this.amount.over(other), this.uncertainty.over(other), this.unit);
	}

	public  DecimalMeasure<E> times(Real other) {
		return new DecimalMeasure<E>(this.amount.times(other), this.uncertainty.times(other), this.unit);
	}

	public DecimalMeasure<E>  one() {
		return exact(amount.one(),this.unit);
	}

	public DecimalMeasure<E>  zero() {
		return exact(amount.zero(),this.unit);
	}
	
	public <T extends Measurable> DecimalMeasure<T> times(DecimalMeasure<?> other) {
		Unit<T> u = this.unit.times(other.unit());
		return new DecimalMeasure<T>(this.amount().times(other.amount()),timesError(other),u);
	}
	
	public <T extends Measurable> DecimalMeasure<T> over(DecimalMeasure<?> other) {
		Unit<T> u = this.unit.over(other.unit());
		return new DecimalMeasure<T>(this.amount().over(other.amount()),overError(other),u);
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
		return new DecimalMeasure<E>(this.amount.minus(other.amount()),this.uncertainty.plus(other.uncertainty),this.unit.minus(other.unit()));
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

	@Override
	public DecimalMeasure<E> convertTo(Unit<E> newUnit) {
		return UnitConversion.convert(this, newUnit);
	}
	
	@Override
	public DecimalMeasure<E> over(Real other, Unit<E> unit) {
		return new DecimalMeasure<E>(this.amount.over(other), this.uncertainty.over(other), unit);
	}

	@Override
	public DecimalMeasure<E> times(Real other, Unit<E> unit) {
		return new DecimalMeasure<E>(this.amount.times(other), this.uncertainty.times(other), unit);
	}

	public <T extends Measurable> DecimalMeasure<T> sqrt(Unit<T> unit) {
		Real r = Real.valueOf(Math.sqrt(this.amount.asNumber().doubleValue()));
		Real u = Real.valueOf(Math.sqrt(this.uncertainty.asNumber().doubleValue()));
		return new DecimalMeasure<T>(r, u, unit);  
	}
	


}
