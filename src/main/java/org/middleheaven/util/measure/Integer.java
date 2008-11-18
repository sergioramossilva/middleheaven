package org.middleheaven.util.measure;

import org.middleheaven.math.structure.Field;
import org.middleheaven.util.Incrementable;
import org.middleheaven.util.Range;


/**
 * Represents an integer number
 * 
 * @author Sergio M.M. Taborda
 *
 */
public abstract class Integer extends Number<Integer> implements Field<Integer> ,  Numerable<Integer> , Incrementable <Integer>{


	private static final long serialVersionUID = 8636156681654308959L;

	public static Integer valueOf (String value) {
		return NumberFactory.getFactory().numberFor(Integer.class , value);
	}
	
	public static Integer valueOf (Number<?> other) {
		if (other instanceof Integer){
			return (Integer)other;
		} 
		return valueOf(other.asNumber().longValue());
	}
	
	public static Integer valueOf (java.lang.Number other) {
		return valueOf(other.longValue());
	}
	
	public static Integer valueOf (long other) {
		return (Integer)NumberFactory.getFactory().numberFor(Integer.class, Long.toString(other));
	}
   
	protected final int rank(){
		return 0;
	}
	
	public final boolean isOdd(){
		return !isEven();
	}
	
	public abstract boolean isEven();
	
	@Override
	public Number<Integer> promote(Number<?> other) {
		return valueOf(other);
	}
	
	public Range<Integer> upTo(Integer other){
		return Range.over(this, other, other.over(other));
	}
	
	public Range<Integer> upTo(Integer other, Integer increment){
		return Range.over(this, other, increment);
	}
}
