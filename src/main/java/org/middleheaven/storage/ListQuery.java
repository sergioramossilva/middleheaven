package org.middleheaven.storage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListQuery<T> implements Query<T> , Serializable{

	private static final long serialVersionUID = 5921463303407006568L;
	
	private List<T> list;
	
	public ListQuery(List<T> list) {
		super();
		this.list = list;
	}

	@Override
	public long count() {
		return list.size();
	}

	@Override
	public T find() {
		if (list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@Override
	public Collection<T> list() {
		return Collections.unmodifiableList(list);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Query<T> setRange(int startAt, int maxCount) {
		return new ListQuery(this.list.subList(startAt, Math.min(list.size(), list.size()-startAt+maxCount)));
	}

}
