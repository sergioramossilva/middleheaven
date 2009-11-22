package org.middleheaven.util.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import org.middleheaven.util.classification.Classifier;

public class TransformCollection<O,T> extends AbstractCollection<T> {

	private Collection<O> original;
	private Classifier<T, O> classifier;
	
	public TransformCollection(Collection<O> original, Classifier<T,O> classifier){
		this.original = original;
		this.classifier = classifier;
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
				return classifier.classify(originalIt.next());
			}

			@Override
			public void remove() {
				originalIt.remove();
			}
			
		};
	}

	@Override
	public int size() {
		return original.size();
	}

}