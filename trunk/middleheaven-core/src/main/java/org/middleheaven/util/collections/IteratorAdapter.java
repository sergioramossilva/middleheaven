package org.middleheaven.util.collections;

import java.util.Iterator;

/**
 * Adapts an Iterator of type <code>A</code> to an Iterator of type <code>T</code>.
 * 
 * @param <T> the type of objects for the iterator
 * @param <A> the type of object that actually will be iterated.
 */
public abstract class IteratorAdapter<T,A> implements Iterator<T> {

	private Iterator<A> other;
	
	/**
	 * 
	 * Constructor.
	 * @param other the iterator to adapt.
	 */
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


	@Override
	public final void remove() {
		other.remove();
	}

	protected abstract T adaptNext(A next);
	
}
