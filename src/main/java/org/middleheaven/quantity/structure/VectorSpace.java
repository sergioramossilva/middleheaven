package org.middleheaven.quantity.structure;

public interface VectorSpace<V,F extends Field<F>> extends GroupAdditive<V>{

	
	public V times (F a);
}
