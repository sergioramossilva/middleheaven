package org.middleheaven.util.measure;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.util.measure.measures.Measurable;



public class CompositeUnit<E extends Measurable> extends Unit<E>{

	
	public static <T extends Measurable> Unit<T> raise (Unit<?> unit , int exponent){
		if (exponent==0){
			return SI.DIMENTIONLESS.cast();
		} else if (exponent>0){
			 Unit<T> a = unit.cast();
			 for (int i = 1 ; i < exponent ; i++){
				 a = a.times(a);
			 }
			 return a;
		} else {
			Unit<T> a = unit.cast();
			 for (int i = 1 ; i < -exponent ; i++){
				 a = a.over(a);
			 }
			 return a;
		}
	}
	
	public static <T extends Measurable> Unit<T> root (Unit<?> unit , int exponent){
		if (exponent==0){
			return SI.DIMENTIONLESS.cast();
		} else if (exponent>0){
			 Unit<T> a = unit.cast();
			 for (int i = 1 ; i < exponent ; i++){
				 a = a.times(a);
			 }
			 return a;
		} else {
			Unit<T> a = unit.cast();
			 for (int i = 1 ; i < -exponent ; i++){
				 a = a.over(a);
			 }
			 return a;
		}
	}
	
	public static <T extends Measurable> Unit<T> times(Unit<?> a , Unit<?> b){
		CompositeUnit<T> c = new CompositeUnit<T>();
		c.add(a,1);
		c.add(b,1);
		return c.simplify();
	}
	
	public static <A extends Measurable, B extends Measurable, T extends Measurable> Unit<T>  over(Unit<A> a , Unit<B> b){
		CompositeUnit<T> c = new CompositeUnit<T>();
		c.add(a,1);
		c.add(b,-1);
		return c.simplify();
	}
	
	public static <T extends Measurable> Unit<T> invert(Unit<?> unit) {
		CompositeUnit<T> c = new CompositeUnit<T>();
		c.add(unit,-1);
		return c.simplify();
	}
	

	private TreeMap<String,UnitPower> units = new TreeMap<String,UnitPower>();
	
	private Dimension<E> currentDimention = Dimension.DIMENTIONLESS.;
	
	CompositeUnit(){}
	CompositeUnit(CompositeUnit other){
		//	clone 
		this.units = (TreeMap) other.units.clone();
		this.currentDimention = other.currentDimention;

	}
	
	private void add(Unit other , int sign){
		if (other instanceof CompositeUnit){
			//	 merge compositions
    		CompositeUnit c = (CompositeUnit)other;

    		for (Map.Entry<String,UnitPower> entry : c.units.entrySet()){
    			UnitPower p = entry.getValue();
    			UnitPower q = units.get(p.unit.symbol());
    			if (q==null){
    				p = new UnitPower(p.unit(),sign * p.exponent());
    				units.put(p.unit.symbol(), p);
    			} else {
    				p = new UnitPower(p.unit(),q.exponent + sign * p.exponent());
    				units.put(p.unit.symbol(), p);
    			}
    		}

    	} else {
    		
    		final String symbol = other.symbol();
			UnitPower c = units.get(symbol);
    		if (c==null){
    			units.put(symbol, new UnitPower(other,sign));
    		} else {
    			units.put(symbol, new UnitPower(c.unit,c.exponent+sign));
    		}
    		
    		
    	}
		
		if (sign>0){
			currentDimention = currentDimention.times(other.dimension());
		} else {
			currentDimention = currentDimention.over(other.dimension());
		}
	}
	
	public Dimension<E> dimension() {
		return currentDimention;
	}

	public boolean isCompatible(Unit<?> other) {
		return this.dimension().equals(other.dimension());
	}
	
	public Unit over(Unit other) {
		CompositeUnit result = new CompositeUnit(this);
    	result.add(other, -1);
    	return result.simplify();
	}
	
	public Unit times(Unit other) {
		CompositeUnit result = new CompositeUnit(this);
    	result.add(other, 1);
    	return result.simplify();
	}


	public boolean equals(Unit other) {
		return other instanceof CompositeUnit && equals((CompositeUnit)other);
	}

	public boolean equals(CompositeUnit other) {
		return this.currentDimention.equals(other.currentDimention);
	}
	
	public Unit minus(Unit other) throws IncompatibleUnitsException {
		return other;
	}

	public Unit plus(Unit other) throws IncompatibleUnitsException {
		return other;
	}


	public String symbol() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<UnitPower> it = units.values().iterator(); it.hasNext();){
			builder.append(it.next().toString());
		}
		return builder.toString();
	}
	
	public Unit<E> simplify(){
		//	elimina todos os dimentionless
		for (Iterator<UnitPower> it = units.values().iterator(); it.hasNext();){
			UnitPower d = it.next();
			if (d.exponent==0){
				it.remove();
			}
		}
		
		// else 
		return this;
	}
	
	public String toString(){
		return symbol();
	}

	@Override
	public Unit raise(int value) {
		return raise (this, value);
	}

	@Override
	public <C extends Measurable> Unit<C> cast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Unit<E> other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Unit<E> minus(Unit<E> other) throws IncompatibleUnitsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Measurable> Unit<T> over(Unit<?> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Unit<E> plus(Unit<E> other) throws IncompatibleUnitsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Measurable> Unit<T> times(Unit<?> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Unit<E> other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Unit<E> minus(Unit<E> other) throws IncompatibleUnitsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Measurable> Unit<T> over(Unit<?> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Unit<E> plus(Unit<E> other) throws IncompatibleUnitsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C extends Measurable> Unit<C> raise(int exponent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C extends Measurable> Unit<C> sqrt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Measurable> Unit<T> times(Unit<?> other) {
		// TODO Auto-generated method stub
		return null;
	}


}
