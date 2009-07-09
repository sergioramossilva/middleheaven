package org.middleheaven.storage;

import java.util.Collection;

public interface Query<T> {

	
	public T find();
	public Collection<T> findAll();
	public long count();
	public boolean isEmpty();
	public Query<T> setRange(int startAt, int maxCount);
}