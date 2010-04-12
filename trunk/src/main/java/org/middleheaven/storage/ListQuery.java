package org.middleheaven.storage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class ListQuery<T> implements Query<T> , Serializable{

	private static final long serialVersionUID = 5921463303407006568L;
	
	public ListQuery() {
		super();
	}

	protected abstract List<T> list();
	
	@Override
	public long count() {
		return list().size();
	}

	@Override
	public T fetchFirst() {
		final List<T> list = list();
		if (list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@Override
	public Collection<T> fetchAll() {
		return Collections.unmodifiableList(list());
	}

	@Override
	public boolean isEmpty() {
		return list().isEmpty();
	}

	@Override
	public Query<T> limit(final int startAt, final int maxCount) {
		
		if (startAt < 1){
			throw new IllegalArgumentException("Range must start at position 1 or further");
		}
		
		return new ListQuery<T>(){

			@Override
			protected List<T> list() {
				final List<T> list = ListQuery.this.list();
				return list.subList(startAt-1, Math.min(list.size(),startAt+maxCount-1));
			}
			
		};
	}


}
