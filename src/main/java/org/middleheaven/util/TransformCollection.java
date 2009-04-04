package org.middleheaven.util;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public abstract class TransformCollection<O,T> extends AbstractCollection<T> {

	private Collection<O> original;
	
	public TransformCollection(Collection<O> original){
		this.original = original;
	}
	
	
	protected Iterator<O> getOriginalIterator(){
		return original.iterator();
	}
	
	@Override
	public Iterator<T> iterator() {
		final Iterator<O> originalIt =getOriginalIterator();
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
	public int size() {
		return original.size();
	}

}
