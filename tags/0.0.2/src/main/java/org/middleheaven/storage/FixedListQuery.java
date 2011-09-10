package org.middleheaven.storage;

import java.util.Collection;
import java.util.List;

import org.middleheaven.util.collections.CollectionUtils;

public class FixedListQuery<T> extends ListQuery<T> {

	private List<T> list;
	
	public static <S> FixedListQuery<S> from(Collection<S> list){
		return new FixedListQuery<S>();
	}

	private FixedListQuery() {
		super();
		this.list = CollectionUtils.ensureSortable(list);
	}

	@Override
	protected List<T> list() {
		return list;
	}

}
