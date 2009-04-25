package org.middleheaven.storage.pagination;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.storage.Query;

public class MappedPaginationService implements PaginationService {

	private Map<String, Integer> sizes = new HashMap<String,Integer>();

	private int defaultPageSize;

	public MappedPaginationService(){
		this(20);
	}

	public MappedPaginationService(int defaultPageSize){
		this.defaultPageSize = defaultPageSize;
	}

	public void setPageSize(String name, int size){
		sizes.put(name, size);
	}

	@Override
	public <T> Paginator<T> paginate(Query<T> query) {
		return paginate(query, "");
	}

	@Override
	public <T> Paginator<T> paginate(Query<T> query, String name) {
		Integer size = sizes.get(name);

		if (size ==null){
			size = defaultPageSize;
		}

		return paginate(query, size);
	}

	@Override
	public <T> Paginator<T> paginate(Query<T> query, int itemsPerPage) {

		return new Paginator<T>(query,itemsPerPage);

	}

}
