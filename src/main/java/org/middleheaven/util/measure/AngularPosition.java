package org.middleheaven.util.measure;

import java.math.BigDecimal;

import org.middleheaven.util.Interval;
import org.middleheaven.util.measure.measures.Angle;

/**
 * Angular position in radians
 * 
 * 
 */
public class AngularPosition extends DecimalMeasure<Angle>  {


	public static AngularPosition fullCircle(){
		return radians(2*Math.PI);
	}
	
	public static AngularPosition halfCircle(){
		return radians(Math.PI);
	}
	
	
	public static AngularPosition arctan(Real value){
		return radians(Math.atan(value.asNumber().doubleValue()));
	}
	
	public static AngularPosition arctan(DecimalMeasure<?> value){
		return arctan(value.amount);
	}

	/**
	 * @param amount angle in radians
	 * @return <code>AngularPosition</code> with value equal to amount in radians
	 */
	public static AngularPosition radians(double amount){
		return new AngularPosition(
				Real.valueOf(amount),
				Real.ZERO(),
				SI.RADIANS
		);
	}

	/**
	 * @param amount angle in degrees
	 * @return <code>AngularPosition</code> with value equal to amount in degrees
	 */
	public static AngularPosition degrees(double amount){
		// convert to radians
		return new AngularPosition(
				Real.valueOf(amount),
				Real.ZERO(),
				SI.DEGREE
		);
	}

	/**
	 * @param amount angle in radians
	 * @return <code>AngularPosition</code> with value equal to amount in radians
	 */
	public static AngularPosition radians(Real amount){
		return new AngularPosition(amount, amount.zero(),SI.RADIANS);
	}

	/**
	 * @param amount angle in degrees
	 * @return <code>AngularPosition</code> with value equal to amount in degrees
	 */
	public static AngularPosition degrees(Real amount){
		// convert to radians
		return new AngularPosition(
				Real.valueOf(amount.asNumber().doubleValue()),
				amount.zero(),
				SI.DEGREE
		);
	}

	protected AngularPosition(Real amount, Real uncertainty, Unit<Angle> unit) {
		super(amount, uncertainty, unit);
	}

	public AngularPosition negate() {
		return new AngularPosition (this.amount.negate(),this.uncertainty, this.unit);
	}


	public AngularPosition  over(Real other) {
		return new AngularPosition (this.amount.over(other), this.uncertainty.over(other), this.unit);
	}

	public AngularPosition  times(Real other) {
		return new AngularPosition (this.amount.times(other), this.uncertainty.times(other), this.unit);
	}

	public AngularPosition   one() {
		return new AngularPosition (this.amount.one(), this.amount.zero(), this.unit);
	}

	public AngularPosition   zero() {
		return new AngularPosition (this.amount.zero(), this.amount.zero(), this.unit);
	}

	public AngularPosition  plus(AngularPosition  other) throws IncompatibleUnitsException {
		assertCompatible (other);
		return new AngularPosition (other.amount().plus(this.amount), this.uncertainty.plus(other.uncertainty) , this.unit.plus(other.unit()));
	}

	public AngularPosition  minus(AngularPosition  other) throws IncompatibleUnitsException {
		assertCompatible (other);
		return new AngularPosition (other.amount().minus(this.amount),this.uncertainty.plus(other.uncertainty),this.unit.minus(other.unit()));
	}

	public AngularPosition toRadians(){
		return (AngularPosition)super.convertTo(SI.RADIANS);
	}

	public AngularPosition toDegrees(){
		return (AngularPosition)super.convertTo(SI.DEGREE);
	}

	public Real sin(){
		return Real.valueOf(Math.sin(this.amount.asNumber().doubleValue()));
	}

	public Real cos(){
		return Real.valueOf(Math.cos(this.amount.asNumber().doubleValue()));
	}

	public Real tan(){
		return Real.valueOf(Math.tan(this.amount.asNumber().doubleValue()));
	}

	public int compareTo(AngularPosition other) {
		assertCompatible (other);
		return this.amount.compareTo(other.amount);
	}

	/**
	 * 
	 * @return returns an angular position in [0 , 2pi[ 
	 */

	public AngularPosition reduce(){


		Interval<Real> range ;

		if (this.unit.equals(SI.RADIANS)){
			range = Interval.between(Real.ZERO(), Real.valueOf(Math.PI*2));
		} else { 
			range = Interval.between(Real.ZERO(), Real.valueOf(360));
		}

		BigDecimal top = range.end().asNumber();
		
		if (this.amount.compareTo(range.start())<0){
			// under range
			BigDecimal cycles = this.amount.asNumber().abs().divideToIntegralValue(top);
			
			cycles = cycles.add(BigDecimal.ONE);

			return new AngularPosition(Real.valueOf(amount.plus(Real.valueOf(cycles.multiply(top)))) , this.uncertainty , this.unit);

		} else if (this.amount.compareTo(range.end())>=0){
			// out of range
			BigDecimal cycles = this.amount.asNumber().divideToIntegralValue(top);

			return new AngularPosition(Real.valueOf(amount.minus(Real.valueOf(cycles.multiply(top)))) , this.uncertainty , this.unit);

		} else {
			return this;
		}


	}

	@Override
	public AngularPosition over(Real other, Unit<Angle> unit) {
		return new AngularPosition(this.amount.over(other), this.uncertainty.over(other), unit);
	}

	@Override
	public AngularPosition times(Real other, Unit<Angle> unit) {
		return new AngularPosition(this.amount.times(other), this.uncertainty.times(other), unit);
	}

}
