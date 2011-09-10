package org.middleheaven.quantity.unit;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.measure.Measurable;

public class MultipleUnit<E extends Measurable> extends Unit<E> {

	private static final  Map<Real, String> PREFIXES = new HashMap<Real,String>();
	
	static {
		PREFIXES.put(Real.valueOf("1000000000"), "G"); // giga
		PREFIXES.put(Real.valueOf("1000000"), "M"); // mega
		PREFIXES.put(Real.valueOf("1000"), "K"); // kilo
		PREFIXES.put(Real.valueOf("100"), "h"); // hecto
		PREFIXES.put(Real.valueOf("0.1"), "d"); // deci
		PREFIXES.put(Real.valueOf("0.01"), "c"); // centi
		PREFIXES.put(Real.valueOf("0.001"), "m"); // mili
		PREFIXES.put(Real.valueOf("0.000001"), "u"); // micro
		PREFIXES.put(Real.valueOf("0.000000001"), "n"); // nano
	}
	private Real scale;
	private Unit<E> base;

	public static <E extends Measurable> MultipleUnit<E> multipleOf(Unit<E> base , Real scale){
		return new MultipleUnit<E>(scale, base);
	}

	/**
	 * unit = scale * base 
	 * @param scale
	 * @param base
	 */
	private MultipleUnit ( Real scale , Unit<E>  base  ){
		this.scale = scale;
		this.base = base;
	}

	@Override
	public Dimension<E> dimension() {
		return base.dimension();
	}
	
	public Unit<E> getBaseUnit(){
		return base;
	}

	@Override
	public boolean equals(Unit<?> other) {
		if (other instanceof MultipleUnit){
			return ((MultipleUnit)other).scale.equals(this.scale) && ((MultipleUnit)other).base.equals(this.base); 
		} else {
			return this.scale.equals(Real.ONE()) && ((MultipleUnit)other).base.equals(this.base);
		}
	}
	
	public DecimalMeasure<E> reduceToUnit(){
		return DecimalMeasure.exact(this.scale, base);
	} 
	


	@Override
	public <T extends Measurable> Unit<T> raise(int exponent) {
		Unit<T> raise2 = CompositeUnit.raise (base, exponent);
		return new MultipleUnit<T>(this.scale.raise(exponent), raise2);
	}

	@Override
	public String symbol() {
		String prefix = PREFIXES.get(scale);
		return (prefix == null ?"" : prefix )+ base.symbol();
	}

	@Override
	public String toString() {
		return symbol();
	}

	public Real getScale() {
		return scale;
	}

}
