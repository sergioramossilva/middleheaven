/**
 * 
 */
package org.middleheaven.util.collections;

import java.util.Iterator;

import org.middleheaven.util.classification.Classifier;

/**
 * 
 */
class TransformedIterable<T, C> implements Iterable<C> {

	private Iterable<T> iterable;
	private Classifier<C, T> classifier;

	/**
	 * Constructor.
	 * @param iterable
	 * @param classifier
	 */
	public TransformedIterable(Iterable<T> iterable, Classifier<C, T> classifier) {
		this.iterable = iterable;
		this.classifier = classifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<C> iterator() {
		return  TransformedIterator.<C,T>transform(iterable.iterator(), classifier);
	}

}
