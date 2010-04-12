package org.middleheaven.storage;

import java.util.Collection;

import org.middleheaven.storage.criteria.Criteria;

public final class SimpleExecutableQuery<T> implements ExecutableQuery<T> {

	Criteria<T> criteria;
	StorableEntityModel model;
	private QueryExecuter executer;

	public SimpleExecutableQuery (Criteria<T> criteria, StorableEntityModel model, QueryExecuter executer ){
		this.criteria = criteria;
		this.model = model;
		this.executer = executer;
	}
	
	/* (non-Javadoc)
	 * @see org.middleheaven.storage.ExecutableQuery#getCriteria()
	 */
	public Criteria<T> getCriteria() {
		return criteria;
	}

	/* (non-Javadoc)
	 * @see org.middleheaven.storage.ExecutableQuery#getModel()
	 */
	public StorableEntityModel getModel() {
		return model;
	}
	
	@Override
	public long count() {
		return fetchAll().size();
	}

	@Override
	public T fetchFirst() {
		if (fetchAll().isEmpty()){
			return null;
		}
		return fetchAll().iterator().next();
	}
	

	@Override
	public boolean isEmpty() {
		return fetchAll().isEmpty();
	}
	

	@Override
	public final Query<T> limit(int startAt, int maxCount) {
		Criteria<T> rangeCriteria = this.criteria.duplicate();
		rangeCriteria.setRange(startAt, maxCount);

		return new SimpleExecutableQuery<T>(rangeCriteria, this.model, this.executer);
	}

	@Override
	public Collection<T> fetchAll() {
		return executer.execute(this);
	}

}
