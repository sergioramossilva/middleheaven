package org.middleheaven.util;

import java.util.Iterator;

public abstract class IteratorAdapter<T,A> implements Iterator<T> {

	Iterator<A> other;
	public IteratorAdapter(Iterator<A> other){
		this.other = other;
	}
	
	@Override
	public final boolean hasNext() {
		return other.hasNext();
	}

	@Override
	public final T next() {
		return adaptNext(other.next());
	}

	public abstract T adaptNext(A next);
	
	@Override
	public final void remove() {
		other.remove();
	}

}
