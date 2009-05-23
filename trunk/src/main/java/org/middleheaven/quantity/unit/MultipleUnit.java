package org.middleheaven.quantity.unit;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.measure.Measure;

public class MultipleUnit<E extends Measurable> extends Unit<E> {

	private final static Map<Real, String> prefixes = new HashMap<Real,String>();
	
	static {
		
		prefixes.put(Real.valueOf("1000"), "K"); // kilo
		prefixes.put(Real.valueOf("0.001"), "m"); // mili
		
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
		return prefixes.get(scale) + base.symbol();
	}

	@Override
	public String toString() {
		return symbol();
	}

	public Real getScale() {
		return scale;
	}

}
