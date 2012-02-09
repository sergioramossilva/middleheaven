package org.middleheaven.domain.query;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public abstract class ListQuery<T> implements Query<T> , Serializable{

	private static final long serialVersionUID = 5921463303407006568L;
	
	public ListQuery() {
		super();
	}

	private List<T> secureList(){
		List<T> list = this.list();
		if (list == null){
			return Collections.emptyList();
		}
		return list;
	}
	
	protected abstract List<T> list();
	
	@Override
	public long count() {
		return secureList().size();
	}

	@Override
	public T fetchFirst() {
		final List<T> list = secureList();
		if (list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@Override
	public Collection<T> fetchAll() {
		return Collections.unmodifiableList(secureList());
	}

	@Override
	public boolean isEmpty() {
		return secureList().isEmpty();
	}

	@Override
	public Query<T> limit(final int startAt, final int maxCount) {
		
		if (startAt < 1){
			throw new IllegalArgumentException("Range must start at position 1 or further");
		}
		
		return new ListQuery<T>(){

			private static final long serialVersionUID = 132895192815795232L;

			@Override
			protected List<T> list() {
				final List<T> list = ListQuery.this.secureList();
				return list.subList(startAt-1, Math.min(list.size(),startAt+maxCount-1));
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return secureList().iterator();
	}

}
