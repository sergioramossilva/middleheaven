package org.middleheaven.storage.pagination;

import java.util.Collection;

import org.middleheaven.storage.Query;

public class QueryPaginatorModel<E> implements PaginatorModel<E> {

	
	
	public static <T> QueryPaginatorModel<T> forQuery(Query<T> query){
		return new QueryPaginatorModel<T>(query);
	}
	
	private Query<E> query;
	
	

	private QueryPaginatorModel(Query<E> query){
		this.query = query;
	}
	
	@Override
	public long totalCount() {
		return query.count();
	}
	
	public Collection<E> getRange(int startAt, int itemsPerPage){

		return query.setRange(startAt, itemsPerPage).all();

	}

}
