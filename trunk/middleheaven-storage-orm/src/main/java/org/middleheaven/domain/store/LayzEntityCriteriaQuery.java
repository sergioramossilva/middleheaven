/**
 * 
 */
package org.middleheaven.domain.store;

import java.util.Collection;
import java.util.Iterator;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.query.Query;
import org.middleheaven.domain.query.QueryExecuter;



/**
 * 
 */
public class LayzEntityCriteriaQuery<T> implements Query<T>{

	private EntityCriteria<T> criteria;
	private QueryExecuter queryExecuter;

	/**
	 * Constructor.
	 * @param criteria
	 * @param strategy
	 * @param queryExecuter
	 */
	public LayzEntityCriteriaQuery(EntityCriteria<T> criteria,QueryExecuter queryExecuter) {
		this.criteria = criteria;
		this.queryExecuter = queryExecuter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T fetchFirst() {
		Collection<T> result = fetchAll();
		if (result.isEmpty()){
			return null;
		} else {
			return result.iterator().next();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<T> fetchAll() {
		return this.queryExecuter.execute(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long count() {
		return fetchAll().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return fetchAll().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query<T> limit(int startAt, int maxCount) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

}
