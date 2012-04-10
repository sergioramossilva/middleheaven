package org.middleheaven.util.collections;

import java.util.Iterator;

import org.middleheaven.util.classification.Classifier;

/**
 * Adapts an Iterator of type <code>A</code> to an Iterator of type <code>T</code>.
 * 
 * @param <T> the type of objects for the iterator
 * @param <A> the type of object that actually will be iterated.
 */
public final class TransformedIterator<T,A> implements Iterator<T> {

	
	public static <TRANSFORMED, ORIGINAL> TransformedIterator<TRANSFORMED, ORIGINAL> transform(Iterator<ORIGINAL> original, Classifier<TRANSFORMED,ORIGINAL> classifier ){
		return new TransformedIterator<TRANSFORMED, ORIGINAL>(original, classifier);
	}
	
	private Iterator<A> other;
	private Classifier<T, A> classifier;
	
	
	/**
	 * 
	 * Constructor.
	 * @param other the iterator to adapt.
	 */
	public TransformedIterator(Iterator<A> other, Classifier<T,A> classifier){
		this.other = other;
		this.classifier = classifier;
	}
	
	@Override
	public final boolean hasNext() {
		return other.hasNext();
	}

	@Override
	public final T next() {
		return this.classifier.classify(other.next());
	}


	@Override
	public final void remove() {
		other.remove();
	}

	
}
