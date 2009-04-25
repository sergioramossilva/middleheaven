package org.middleheaven.storage.pagination;

import org.middleheaven.storage.Query;

public interface PaginationService {

	
	public <T> Paginator<T> paginate(Query<T> query);
	public <T> Paginator<T> paginate(Query<T> query, String name);
	public <T> Paginator<T> paginate(Query<T> query, int itemsPerPage);
}
