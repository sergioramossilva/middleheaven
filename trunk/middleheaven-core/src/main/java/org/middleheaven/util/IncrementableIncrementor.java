package org.middleheaven.util;

public final class IncrementableIncrementor<T> implements Incrementor<T> {

	private final T increment;
	public IncrementableIncrementor(T increment){
		this.increment = increment;
	}

	public T increment(T object) {
		return (T) ((Incrementable)object).incrementBy(increment);
	}
	

}
