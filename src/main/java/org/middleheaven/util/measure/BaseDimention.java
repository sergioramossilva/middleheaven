package org.middleheaven.util.measure;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.middleheaven.util.measure.measures.Measurable;

class BaseDimention<E extends Measurable> extends Dimension<E> implements Comparable<BaseDimention<E>>{

	
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
	
	public Dimension times(Dimension other){
		if (exponent==0){
			return other;
		}
		if (other instanceof BaseDimention){
			BaseDimention b = (BaseDimention)other;
			if (b.axis ==  this.axis){
				return new BaseDimention(this.axis , this.exponent + b.exponent).simplify();
			} 
		}
		return CompositeDimention.multiply(this, other);
	}
	
	public Dimension over(Dimension other){
		if (other instanceof BaseDimention){
			if (exponent==0){
				return new BaseDimention(((BaseDimention)other).axis ,  -((BaseDimention)other).exponent).simplify();
			} else if (((BaseDimention)other).axis ==  this.axis){
				return new BaseDimention(this.axis , this.exponent - ((BaseDimention)other).exponent).simplify();
			} 
		}
		return CompositeDimention.over(this, other);
	}
	
	public boolean equals (Object other){
		return other instanceof BaseDimention && equals((BaseDimention)other);
	}
	
	public boolean equals (BaseDimention other){
		return other.exponent == this.exponent && this.axis == other.axis;
	}
	
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
	
	public int compareTo(BaseDimention other) {
		return this.axis==other.axis ?  this.exponent - other.exponent  : this.axis - other.axis;
	}
}
