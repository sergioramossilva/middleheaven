package org.middleheaven.util.measure;

import org.middleheaven.util.Incrementable;
import org.middleheaven.util.Range;
import org.middleheaven.util.measure.structure.Field;


/**
 * Represents an real number 
 * 
 * @author Sergio M.M. Taborda
 */
public abstract class Real extends Number<Real> implements Field<Real> , Incrementable <Real>{



	
	public static Real fraction (int num , int den){
		return valueOf(num).over(valueOf(den));
	}
	
	public static Real[] valueOf(java.lang.Number ... array){
		NumberFactory factory = NumberFactory.getFactory();
		Real[] res = new Real[array.length];
		for (int i =0 ; i < array.length; i++){
			res[i] = factory.numberFor(Real.class, array[i].toString());
		}
		return res;
	}
	

	public static Real ONE(){
		return NumberFactory.getFactory().numberFor( Real.class , "1");
	}

	public static Real ZERO(){
		return NumberFactory.getFactory().numberFor( Real.class, "0");
	}
	
	public static Real valueOf (String value) {
		return NumberFactory.getFactory().numberFor( Real.class, value);
	}

	public static Real valueOf (Number<?> other) {
		if (other instanceof Real){
			return (Real)other;
		} 
		return NumberFactory.getFactory().numberFor(Real.class, other.toString());
	}

	public static Real valueOf (java.lang.Number other) {
		return NumberFactory.getFactory().numberFor(Real.class,other.toString());
	}

	public static Real valueOf (double other) {
		return NumberFactory.getFactory().numberFor( Real.class, Double.toString(other));
	}

	@Override
	public Number<Real> promote(Number<?> other) {
		return NumberFactory.getFactory().promote(other, Real.class); 
	}

	protected final int rank(){
		return 1;
	}
	
	public Range<Real> upTo(Real other){
		return Range.over(this, other, other.over(other));
	}
	
	public Range<Real> upTo(Real other, Real increment){
		return Range.over(this, other, increment);
	}

	public abstract Real sqrt();
	
	/**
	 *  Complex square root. Returns the square root in complex form
	 */
	public Complex csqrt() {
		if (this.compareTo(Real.ZERO())>=0){
			return Complex.valueOf(this.sqrt(), this.zero());
		} else {
			return Complex.valueOf(this.zero(), this.negate().sqrt());
		}
	}
	


}
