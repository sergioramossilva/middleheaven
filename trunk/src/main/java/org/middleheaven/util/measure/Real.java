package org.middleheaven.util.measure;

import org.middleheaven.util.measure.structure.Field;


/**
 * Represents an real number 
 * 
 * @author Sergio M.M. Taborda
 */
public abstract class Real extends Number<Real> implements Field<Real> {


	public static Real fraction (int num , int den){
		return valueOf(num).over(valueOf(den));
	}
	
	public static Real[] valueOf(java.lang.Number ... array){
		NumberFactory factory = NumberFactory.getFactory();
		Real[] res = new Real[array.length];
		for (int i =0 ; i < array.length; i++){
			res[i] = factory.numberFor(array[i].toString(), Real.class);
		}
		return res;
	}
	
	
	public static Real ONE(){
		return (Real)NumberFactory.getFactory().one();
	}

	public static Real ZERO(){
		return (Real)NumberFactory.getFactory().zero();
	}
	
	public static Real valueOf (String value) {
		return NumberFactory.getFactory().numberFor(value, Real.class);
	}

	public static Real valueOf (Number<?> other) {
		if (other instanceof Real){
			return (Real)other;
		} 
		return (Real)NumberFactory.getFactory().numberFor(other.toString(), Real.class);
	}

	public static Real valueOf (java.lang.Number other) {
		return (Real)NumberFactory.getFactory().numberFor(other.toString(), Real.class);
	}

	public static Real valueOf (double other) {
		return (Real)NumberFactory.getFactory().numberFor(Double.toString(other), Real.class);
	}

	@Override
	public Number<Real> promote(Number<?> other) {
		return valueOf(other);
	}

	protected final int rank(){
		return 1;
	}
	
}
