package org.middleheaven.text.indexing;

/**
 * The result of search in a text index
 *
 * @param <T>
 */
public interface SearchHits<T> extends Iterable<SearchHit<T>> {

	public boolean isEmpty();
	
	public int getSize();
	
}
