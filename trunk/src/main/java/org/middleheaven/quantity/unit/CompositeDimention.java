package org.middleheaven.quantity.unit;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("unchecked")
public class CompositeDimention extends Dimension {

	/*
	 * Due to limitation of the model dimension generic type can not be atributed 
	 * so the class is all marked with @SuppressWarnings("unchecked") 
	 */
	
	private static final long serialVersionUID = 963244403057787828L;


	public static Dimension multiply(Dimension a, Dimension b){
		CompositeDimention c = new  CompositeDimention();
		c.add(a, 1);
		c.add(b, 1);
		return c.simplify();
	}
	
	public static Dimension over(Dimension a, Dimension b){
		CompositeDimention c = new  CompositeDimention();
		c.add(a, 1);
		c.add(b, -1);
		return c.simplify();
	}


	private TreeMap<Character , BaseDimention > dims = new TreeMap<Character , BaseDimention >();


	CompositeDimention(CompositeDimention other){
		// clone 
		 this.dims = (TreeMap) other.dims.clone();

	}

	
	CompositeDimention(){
		this.dims.put(Character.valueOf('1'), BaseDimention.class.cast(Dimension.DIMENTIONLESS));
	}
	
	
	private void add(Dimension<?> other , int sign){
		if (other instanceof BaseDimention){
			BaseDimention<?> baseDim = (BaseDimention<?>)other; 
    		BaseDimention<?> c = dims.get(baseDim.axis());
    		if (c!=null){
    			dims.put(baseDim.axis(), BaseDimention.class.cast(BaseDimention.base(c.axis(),c.exponent()+sign*baseDim.exponent()).simplify()));
    		} else {
    			dims.put(baseDim.axis(), BaseDimention.base(baseDim.axis(),sign*baseDim.exponent()));
    		}

    	} else {
    		// merge compositions
    		CompositeDimention c = (CompositeDimention)other;

    		for (Map.Entry<Character , BaseDimention > entry : c.dims.entrySet()){
    			add(entry.getValue(),sign );
    		}
    	
    	}
	}
	
	@Override
    public Dimension times(Dimension other){
		CompositeDimention result = new CompositeDimention(this);
		result.add(other, 1);
    	return result.simplify();
    }


    @Override
    public Dimension over(Dimension other){
    	CompositeDimention result = new CompositeDimention(this);
    	result.add(other, -1);
    	return result.simplify();
    }
    
	@Override
	protected Dimension simplify() {
		// elimina todos os dimentionless
		for (Iterator<BaseDimention> it = dims.values().iterator(); it.hasNext();){
			BaseDimention d = it.next();
			if (d.exponent()==0 || d.axis() == '1'){
				it.remove();
			}
		}
		
		// if there is only one
		if (dims.size()==1){
			return dims.values().iterator().next().simplify();
		}
		
		// else 
		return this;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder(); 
		for (Iterator<BaseDimention> it = dims.values().iterator(); it.hasNext();){
			builder.append(it.next().toString());
		}
		return builder.toString();
	}
	
	public boolean equals (Object other){
		return other instanceof Dimension && this.toString().equals(other.toString());
	}
}
