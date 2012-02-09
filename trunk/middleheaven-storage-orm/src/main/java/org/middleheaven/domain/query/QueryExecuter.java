package org.middleheaven.domain.query;

import java.util.Collection;

import org.middleheaven.domain.criteria.EntityCriteria;


public interface QueryExecuter {

	
	public <T> Collection<T> execute(EntityCriteria<T> query);
}
