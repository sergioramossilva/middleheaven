/**
 * 
 */
package org.middleheaven.util.collections;

import java.util.Iterator;

/**
 * 
 */
public class ArrayEnumerable<T> extends AbstractEnumerable<T> {

	
	private T[] array;

	public ArrayEnumerable (T[] array){
		this.array = array;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator<T>(array);
	}

}
