package org.middleheaven.util.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import org.middleheaven.util.classification.Classifier;

/**
 * Allows type safe transformation from a collection of objects to another, using a {@link Classifier}.
 * 
 * @param <O> original object type
 * @param <T> target object type
 */
public final class TransformedCollection<O,T> extends AbstractCollection<T> {

	private Collection<? extends O> original;
	private Classifier<T, O> classifier;
	
	/**
	 * Creates a TransformedCollection from the original objects and a classifier.
	 * @param <ORIGINAL> the original object type
	 * @param <TRANSFORMED> the target object type
	 * @param original the original collection
	 * @param classifier the classifier that will transform the data
	 * @return the resulting TransformedCollection.
	 */
	public static <ORIGINAL,TRANSFORMED> TransformedCollection<ORIGINAL,TRANSFORMED> transform(Collection<? extends ORIGINAL> original, Classifier<TRANSFORMED,ORIGINAL> classifier){
		return new TransformedCollection<ORIGINAL,TRANSFORMED>(original, classifier);
	}
	
	private TransformedCollection(Collection<? extends O> original, Classifier<T,O> classifier){
		this.original = original;
		this.classifier = classifier;
	}
	
	
	private Iterator<? extends O> getOriginalIterator(){
		return original.iterator();
	}
	
	@Override
	public Iterator<T> iterator() {
		final Iterator<? extends O> originalIt = getOriginalIterator();
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
