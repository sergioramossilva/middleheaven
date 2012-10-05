package org.middleheaven.util.collections;

import org.middleheaven.util.Incrementor;

public class EmptyIncrementor<T> implements Incrementor<T>{

	
	public static <T> Incrementor<T> emptyIncrementor(){
		return new EmptyIncrementor<T>();
	}
	
	private EmptyIncrementor(){}
	
	@Override
	public T increment(T object) {
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<T> reverse() {
		return this;
	}

}
