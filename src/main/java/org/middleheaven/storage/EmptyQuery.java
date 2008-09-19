package org.middleheaven.storage;

import java.util.Collection;
import java.util.Collections;

public final class EmptyQuery<T> implements Query<T>{

	@Override
	public long count() {
		return 0;
	}

	@Override
	public T find() {
		return null;
	}

	@Override
	public Collection<T> list() {
		return Collections.emptyList();
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

}
