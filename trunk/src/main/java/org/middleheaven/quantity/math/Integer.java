package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.MathStructuresFactory;
import org.middleheaven.util.Incrementable;
import org.middleheaven.util.collections.Range;


/**
 * Represents an integer number
 * 
 *
 */
public abstract class Integer extends Numeral<Integer> implements Field<Integer> ,  Comparable<Numeral<? super Integer>>, Enumerable<Integer> , Incrementable <Integer>{


	private static final long serialVersionUID = 8636156681654308959L;

	public static Integer valueOf (String value) {
		try{
		return MathStructuresFactory.getFactory().numberFor(Integer.class , value);
		}catch (NumberFormatException e){
			throw new NumberFormatException(value + " is not an integer");
		}
	}
	
	public static Integer valueOf (Numeral<?> other) {
		if (Integer.class.isInstance(other)){
			return Integer.class.cast(other);
		} 
		return valueOf(other.asNumber().longValue());
	}
	
	public static Integer valueOf (java.lang.Number other) {
		return valueOf(other.longValue());
	}
	
	public static Integer valueOf (long other) {
		return (Integer)MathStructuresFactory.getFactory().numberFor(Integer.class, Long.toString(other));
	}
   
	protected final int rank(){
		return 0;
	}
	
	public final boolean isOdd(){
		return !isEven();
	}
	
	public abstract boolean isEven();
	
	@Override
	public Numeral<Integer> promote(Numeral<?> other) {
		return valueOf(other);
	}
	
	public Range<Integer> upTo(Integer other){
		return Range.over(this, other, other.over(other));
	}
	
	public Range<Integer> upTo(Integer other, Integer increment){
		return Range.over(this, other, increment);
	}
}
