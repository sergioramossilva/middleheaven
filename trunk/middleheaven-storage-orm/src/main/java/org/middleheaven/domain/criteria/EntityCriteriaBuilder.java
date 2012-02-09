package org.middleheaven.domain.criteria;


public class EntityCriteriaBuilder<T> extends AbstractEntityCriteriaBuilder<T, EntityCriteriaBuilder<T>> {

	
	public static <U> EntityCriteriaBuilder<U> search(Class<U> type){
		return new EntityCriteriaBuilder<U>(type);
	}
	
	protected EntityCriteriaBuilder(Class<T> type) {
		super(type);
	}

	private static class BuildedCriteria<T> extends AbstractEntityCriteria<T> {

		public BuildedCriteria(BuildedCriteria<T> other) {
			super(other);
		}

		public BuildedCriteria(Class<T> type) {
			super(type);
		}

		@Override
		public EntityCriteria<T> duplicate() {
			return new BuildedCriteria<T>(this);
		}

	}

	public EntityCriteria<T> distinct(){
		this.criteria.setDistinct(true);
		return this.criteria;
	}
	
	
	/* TODO Can Projections be set only in the criteria it self when needed  
	public CriteriaBuilder<T> sum(String qname){
		this.criteria.add(Projections.sum(QualifiedName.of(qname)));
		return this;
	}

	public CriteriaBuilder<T> max(String qname){
		this.criteria.add(Projections.max(QualifiedName.of(qname)));
		return this;
	}

	 */

	public EntityCriteriaBuilder<T> limit(int count) {
		this.criteria.setRange(count);
		return this;
	}

	public EntityCriteriaBuilder<T> limit(int start, int count) {
		this.criteria.setRange(start,count);
		return this;
	}

}
