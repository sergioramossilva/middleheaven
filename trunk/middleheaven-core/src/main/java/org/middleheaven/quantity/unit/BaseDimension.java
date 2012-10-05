package org.middleheaven.quantity.unit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.middleheaven.quantity.measure.Measurable;

class BaseDimension<E extends Measurable> extends Dimension<E> implements Comparable<BaseDimension<E>>{

	
    private final char axis;
 
    private int exponent = 1;
	
    static <T extends Measurable> BaseDimension<T> base(Character axis){
    	return new BaseDimension<T>(axis,1);
    }
    static <T extends Measurable> BaseDimension<T> base(Character axis,int exponent){
    	return new BaseDimension<T>(axis,exponent);
    }
 
	
    private BaseDimension(Character axis, int exponent){
		this.exponent = exponent;
		this.axis =exponent==0? '1': axis;
	}
	
	Character axis(){
		return axis;
	}
	
	int exponent(){
		return exponent;
	}
	
	public <T extends Measurable> Dimension<T> times(Dimension<?> other){
		if (exponent==0){
			return (Dimension<T>) other;
		}
		if (other instanceof BaseDimension){
			BaseDimension<?> b = (BaseDimension<?>)other;
			if (b.axis ==  this.axis){
				return new BaseDimension<T>(this.axis , this.exponent + b.exponent).simplify();
			} 
		}
		return CompositeDimention.multiply(this, other);
	}
	
	public <T extends Measurable> Dimension<T> over(Dimension<?> other){
		if (other instanceof BaseDimension){
			if (exponent==0){
				return new BaseDimension<T>(((BaseDimension<?>)other).axis ,  -((BaseDimension<?>)other).exponent).simplify();
			} else if (((BaseDimension<?>)other).axis ==  this.axis){
				return new BaseDimension<T>(this.axis , this.exponent - ((BaseDimension<?>)other).exponent).simplify();
			} 
		}
		return CompositeDimention.over(this, other);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean equals (Object other){
		return other instanceof BaseDimension && equalsOther((BaseDimension<?>)other);
	}
	
	private boolean equalsOther (BaseDimension<?> other){
		return other.exponent == this.exponent && this.axis == other.axis;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return this.exponent ^ (int)this.axis;
	}
	

	protected Dimension simplify(){
		if (this.exponent==0){
			return Dimension.DIMENTIONLESS;
		}
		
		Field[] fields = Dimension.class.getFields();
		
		for (Field f : fields){
			if (Modifier.isStatic(f.getModifiers())){
				try {
					Object obj = f.get(this);
					if (obj instanceof BaseDimension && obj.equals(this)){
						return ((BaseDimension<E>)obj);
					}
				} catch (IllegalArgumentException e) {
					assert false : e.getMessage();  // cannot happen
				} catch (IllegalAccessException e) {
					assert false : e.getMessage(); // cannot happen
				}
			}
		}
		return this;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder().append(this.axis);
		if (this.exponent!=1){
			builder.append('^').append(exponent);
		}
		return builder.toString();
	}
	
	public int compareTo(BaseDimension<E> other) {
		return this.axis==other.axis ?  this.exponent - other.exponent  : this.axis - other.axis;
	}
}
