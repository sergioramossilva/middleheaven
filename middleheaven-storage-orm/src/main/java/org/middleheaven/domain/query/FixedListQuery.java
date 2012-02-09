package org.middleheaven.domain.query;

import java.util.Collection;
import java.util.List;

import org.middleheaven.util.collections.CollectionUtils;

public class FixedListQuery<T> extends ListQuery<T> {

	private static final long serialVersionUID = 1192017984116455738L;
	
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
