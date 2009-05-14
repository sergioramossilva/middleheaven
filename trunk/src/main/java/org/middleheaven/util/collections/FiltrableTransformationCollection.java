package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.middleheaven.util.classification.BooleanClassifier;

public abstract class FiltrableTransformationCollection <O,T> extends TransformCollection<O,T> {

	private LinkedList<T> filtraded;
	private BooleanClassifier<T> classifier;
	
	public FiltrableTransformationCollection(Collection<O> original, BooleanClassifier<T> classifier){
		super(original);
		this.classifier = classifier;
	}

	@Override
	public synchronized Iterator<T> iterator() {
		if (filtraded != null){
			return filtraded.iterator();
		}
		
		filtraded = new LinkedList<T>();
		final Iterator<O> originalIt = this.getOriginalIterator();
		return new Iterator <T>(){

			@Override
			public boolean hasNext() {
				while( originalIt.hasNext()){
					T t = transform(originalIt.next());
					if (classifier.classify(t).booleanValue()){
						filtraded.add(t);
						return true;
					}
				}
				return false;
			}

			@Override
			public T next() {
				return filtraded.getLast();
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
		if (filtraded == null){
			for (T t : this){} // just exercise the iterator
		}
		return filtraded.size();
	}

}