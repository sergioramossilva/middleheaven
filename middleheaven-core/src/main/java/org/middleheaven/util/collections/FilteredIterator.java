/**
 * 
 */
package org.middleheaven.util.collections;

import java.util.Iterator;

import org.middleheaven.util.classification.Predicate;

/**
 * 
 */
public class FilteredIterator<T> implements Iterator<T> {

	private Predicate<T> predicate;
	private Iterator<T> original;

	
	private T next;
	
	
	private static class Item<T>{
		
		boolean isFound = true;
		T object;
		
		/**
		 * Constructor.
		 * @param n
		 */
		public Item(T n) {
			this.object = n;
			this.isFound = true;
		}
		
		public Item() {
			this.isFound = false;
		}
		
	}
	/**
	 * Constructor.
	 * @param iterator
	 * @param predicate
	 */
	public FilteredIterator(Iterator<T> iterator, Predicate<T> predicate) {
		this.original = iterator;
		this.predicate = predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (original.hasNext()){
			Item<T> item = fetchNext();
			if (item.isFound){
				next = item.object;
				return true;
			} 
		} 
		return false;
	}

	private Item<T> fetchNext(){
		while(original.hasNext()){
			T n = original.next();
			if (predicate.classify(n).booleanValue()){
				return new Item<T>(n);
			}
		}
		return new Item<T>();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next() {
		return next;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		original.remove();
	}

}
