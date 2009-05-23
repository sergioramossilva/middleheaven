package org.middleheaven.util.collections;

import org.middleheaven.quantity.math.structure.GroupAdditive;

public abstract class Acumulator<N extends GroupAdditive<N>, T> implements Walker<T> {

	private N sum;

	public Acumulator(){}

	public Acumulator(N inicialValue){
		this.sum = inicialValue;
	}

	@Override
	public final void doWith(T object) {
		N value = this.getValue(object);
		if(value != null ){
			if(sum==null){
				sum = value.zero();
			}
			sum = sum.plus(value);
		}
	}

	protected abstract N getValue(T object);


	public N getResult(){
		return sum;
	}
	
	public void reset() {
		this.sum = null;
	}

}
