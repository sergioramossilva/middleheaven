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
		return all().size();
	}

	@Override
	public T first() {
		if (all().isEmpty()){
			return null;
		}
		return all().iterator().next();
	}
	

	@Override
	public boolean isEmpty() {
		return all().isEmpty();
	}
	

	@Override
	public final Query<T> setRange(int startAt, int maxCount) {
		Criteria<T> rangeCriteria = this.criteria.duplicate();
		rangeCriteria.setRange(startAt, maxCount);

		return new SimpleExecutableQuery<T>(rangeCriteria, this.model, this.executer);
	}

	@Override
	public Collection<T> all() {
		return executer.execute(this);
	}

}
