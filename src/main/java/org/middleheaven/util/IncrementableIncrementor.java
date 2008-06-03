package org.middleheaven.util;

public class IncrementableIncrementor<T extends Incrementable<N>,N > implements Incrementor<T> {

	private N increment;
	public IncrementableIncrementor(N increment){
		this.increment = increment;
	}

	public T increment(T object) {
		return object.incrementBy(increment);
	}
	

}
