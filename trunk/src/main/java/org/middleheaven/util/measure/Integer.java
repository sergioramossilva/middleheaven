package org.middleheaven.util.measure;

import org.middleheaven.util.measure.structure.Field;


/**
 * Represents a integer number
 * 
 * @author Sergio M.M. Taborda
 *
 */
public abstract class Integer extends Number<Integer> implements Field<Integer> ,  Numerable<Integer>{

	public static Integer valueOf (String value) {
		return NumberFactory.getFactory().numberFor(value, Integer.class);
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
		return (Integer)NumberFactory.getFactory().numberFor(Long.toString(other), Integer.class);
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
}
