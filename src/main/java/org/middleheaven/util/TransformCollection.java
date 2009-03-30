package org.middleheaven.util;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public abstract class TransformCollection<O,T> extends AbstractCollection<T> {

	private Collection<O> original;
	
	public TransformCollection(Collection<O> original){
		this.original = original;
	}
	
	@Override
	public final Iterator<T> iterator() {
		final Iterator<O> originalIt = original.iterator();
		return new Iterator <T>(){

			@Override
			public boolean hasNext() {
				return originalIt.hasNext();
			}

			@Override
			public T next() {
				return transform(originalIt.next());
			}

			@Override
			public void remove() {
				originalIt.remove();
			}
			
		};
	}

	protected abstract T transform(O object);
	
	@Override
	public final int size() {
		return original.size();
	}

}
