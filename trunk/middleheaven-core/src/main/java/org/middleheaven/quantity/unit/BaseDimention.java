package org.middleheaven.quantity.unit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.middleheaven.quantity.measure.Measurable;

class BaseDimention<E extends Measurable> extends Dimension<E> implements Comparable<BaseDimention<E>>{

	private static final long serialVersionUID = -3990402812522767184L;

	private final char axis;
 
    private int exponent = 1;
	
    static <E extends Measurable> BaseDimention<E> base(Character axis){
    	return new BaseDimention<E>(axis,1);
    }
    
    static <E extends Measurable> BaseDimention<E> base(Character axis, int exponent){
    	return new BaseDimention<E>(axis,exponent);
    }
	
    private BaseDimention(Character axis, int exponent){
		this.exponent = exponent;
		this.axis =exponent==0? '1': axis;
	}
	
	Character axis(){
		return axis;
	}
	
	int exponent(){
		return exponent;
	}
	
	public boolean equals (Object other){
		return other instanceof BaseDimention && equalsOther((BaseDimention<?>)other);
	}
	
	private boolean equalsOther (BaseDimention<?> other){
		return other.exponent == this.exponent && this.axis == other.axis;
	}
	public int hashCode(){
		return this.exponent ^ (int)this.axis;
	}
	
	@SuppressWarnings("unchecked")
	protected Dimension simplify(){
		if (this.exponent==0){
			return Dimension.DIMENTIONLESS;
		}
		
		Field[] fields = Dimension.class.getFields();
		
		for (Field f : fields){
			if (Modifier.isStatic(f.getModifiers())){
				try {
					Object obj = f.get(this);
					if (obj instanceof BaseDimention && ((BaseDimention)obj).equals(this)){
						return (BaseDimention)obj;
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


	@SuppressWarnings("unchecked")
	@Override
	public <T extends Measurable> Dimension<T> over(Dimension<?> other) {
		if (other instanceof BaseDimention){
			if (exponent==0){
				return new BaseDimention(((BaseDimention)other).axis ,  -((BaseDimention)other).exponent).simplify();
			} else if (((BaseDimention)other).axis ==  this.axis){
				return new BaseDimention(this.axis , this.exponent - ((BaseDimention)other).exponent).simplify();
			} 
		}
		return CompositeDimention.over(this, other);
	}


	@Override @SuppressWarnings("unchecked")
	public <T extends Measurable> Dimension<T> times(Dimension<?> other) {
		if (exponent==0){
			return (Dimension<T>) other;
		}
		if (other instanceof BaseDimention){
			BaseDimention b = (BaseDimention)other;
			if (b.axis ==  this.axis){
				return new BaseDimention(this.axis , this.exponent + b.exponent).simplify();
			} 
		}
		return CompositeDimention.multiply(this, other);
	}

	@Override
	public int compareTo(BaseDimention<E> other) {
		return this.axis==other.axis ?  this.exponent - other.exponent  : this.axis - other.axis;
	}
}
