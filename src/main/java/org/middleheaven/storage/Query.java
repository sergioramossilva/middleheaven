package org.middleheaven.storage;

import java.util.Collection;

public interface Query<T> {

	
	public T first();
	
	public Collection<T> all();
	/**
	 * Return the total number of elements in the query.
	 * Even if the query was been paginated, {@code count} will allways return the
	 * total elements count number.
	 * @return the total number of elements in the query
	 */
	public long count();
	
	/**
	 * @return {@code true} if count would return 0, {@code false} otherwise.
	 */
	public boolean isEmpty();
	
	public Query<T> setRange(int startAt, int maxCount);
}
