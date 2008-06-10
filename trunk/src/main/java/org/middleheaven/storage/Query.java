package org.middleheaven.storage;

import java.util.Collection;

public interface Query<T> {

	
	public T find();
	public Collection<T> list();
	public long count();
}
