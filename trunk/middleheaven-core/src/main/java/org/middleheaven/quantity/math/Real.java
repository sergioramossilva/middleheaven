package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.MathStructuresFactory;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;
import org.middleheaven.util.Incrementable;
import org.middleheaven.util.collections.Range;


/**
 * Represents an element of |R  (the real numbers set) 
 * 
 */
public abstract class Real extends Numeral<Real> implements  Comparable<Numeral<? super Real>> ,Incrementable <Real>{

	public static Real fraction (int num , int den){
		return valueOf(num).over(valueOf(den));
	}
	
	public static Real[] valueOf(java.lang.Number ... array){
		MathStructuresFactory factory = MathStructuresFactory.getFactory();
		Real[] res = new Real[array.length];
		for (int i =0 ; i < array.length; i++){
			res[i] = factory.numberFor(Real.class, array[i].toString());
		}
		return res;
	}
	

	public static Real ONE(){
		return MathStructuresFactory.getFactory().numberFor( Real.class , "1");
	}

	public static Real ZERO(){
		return MathStructuresFactory.getFactory().numberFor( Real.class, "0");
	}
	
	public static Real valueOf (String value) {
		return MathStructuresFactory.getFactory().numberFor( Real.class, value);
	}

	public static Real valueOf (Numeral<?> other) {
		if (Real.class.isInstance(other)){
			return Real.class.cast(other);
		} 
		return MathStructuresFactory.getFactory().numberFor(Real.class, other.toString());
	}

	public static Real valueOf (java.lang.Number other) {
		return MathStructuresFactory.getFactory().numberFor(Real.class,other.toString());
	}

	public static Real valueOf (double other) {
		return MathStructuresFactory.getFactory().numberFor( Real.class, Double.toString(other));
	}

	@Override
	public Numeral<Real> promote(Numeral<?> other) {
		return MathStructuresFactory.getFactory().promote(other, Real.class); 
	}

	protected final int rank(){
		return 1;
	}
	
	public Range<Real> upTo(double other){
		return Range.over(this, Real.valueOf(other), one());
	}
	
	public Range<Real> upTo(Real other){
		return Range.over(this, other, one());
	}
	
	public Range<Real> upTo(Real other, Real increment){
		return Range.over(this, other, increment);
	}

	/**
	 * 
	 * @return real square root
	 */
	public abstract Real sqrt();
	
	/**
	 * 
	 * @return real arctang 
	 */
	public abstract Real arctan();
	
	
	public abstract Real sin();

	public abstract Real cos();
	
	public abstract Real tan();

	public abstract Real exp();
	
	public abstract Real ln();

	
	public Real raise(int exponent) {
		if (exponent==0){
			return Real.ONE();
		} else if (exponent>0){
			 Real a = this;
			 Real s = this;
			 for (int i = 1 ; i < exponent ; i++){
				 s = s.times(a);
			 }
			 return s;
		} else {
			 Real a = this;
			 Real s = this;
			 for (int i = 0 ; i <= -exponent ; i++){
				 s = s.over(a);
			 }
			 return s;
		}
	}
	

}
