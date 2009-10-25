package org.middleheaven.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListQuery<T> implements Query<T> , Serializable{

	private static final long serialVersionUID = 5921463303407006568L;
	
	private List<T> list;
	
	public ListQuery(Collection<? extends T> list) {
		super();
		this.list = new ArrayList<T> (list);
	}

	@Override
	public long count() {
		return list.size();
	}

	@Override
	public T first() {
		if (list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@Override
	public Collection<T> all() {
		return Collections.unmodifiableList(list);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Query<T> setRange(int startAt, int maxCount) {
		return new ListQuery<T>(this.list.subList(startAt, Math.min(list.size(), list.size()-startAt+maxCount)));
	}

}
