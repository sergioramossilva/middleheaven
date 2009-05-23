package org.middleheaven.util.collections;

import org.middleheaven.quantity.math.structure.GroupAdditive;

public class NumberAcumulator<N extends GroupAdditive<N>> extends Acumulator<N, N> {

	public static <A extends GroupAdditive<A>> NumberAcumulator<A> instance(){
		return new NumberAcumulator<A>();
	}
	
	public static <A extends GroupAdditive<A>> NumberAcumulator<A> instance(A initValue){
		return new NumberAcumulator<A>(initValue);
	}
	
	private NumberAcumulator(){}
	private NumberAcumulator(N initValue){
		super(initValue);
	}
	
	@Override
	protected final N getValue(N object) {
		return object;
	}



}
