package org.middleheaven.util.sequence;

import java.util.Arrays;
import java.util.Iterator;

public class IterableBasedSequence<T> implements LimitedSequence<T> {
	
	Iterator<T> it;
	
	public static <T> Sequence<T> sequenceFor(T[] array){
		return new IterableBasedSequence<T>(Arrays.asList(array));
	}
	
	public static <T> Sequence<T> sequenceFor(Iterable<T> iterable){
		return new IterableBasedSequence<T>(iterable);
	}
	
	private IterableBasedSequence(Iterable<T> iterable){
		this.it = iterable.iterator();
	}
	
	@Override
	public SequenceToken<T> next() {
		if (!it.hasNext()){
			return null;
		} 
		return new DefaultToken<T>(it.next()); 
	}

	@Override
	public Iterator<T> iterator() {
		return it;
	}

	

}
