package org.middleheaven.storage.criteria;


import org.middleheaven.storage.QualifiedName;

/**
 * An implementation of the
 * <pattern>Builder</pattern> pattern with method chaining
 * to help build a <code>Criteria<T></code> object 
 *
 * @param <T> current object type for the application of restrictions
 * @param <Parent> object type for the resulting criteria.
 */
public class CriteriaBuilder<T> extends CriteriaBuilderStrategy<T,CriteriaBuilder<T>> {

	public static <L> CriteriaBuilder<L> search(Class<L> type) {
		return new CriteriaBuilder<L>(type);
	}

	AbstractCriteria<T> criteria;

	protected CriteriaBuilder(Class<T> type){
		this.criteria = new BuildedCriteria<T>(type);
	}

	private static class BuildedCriteria<T> extends AbstractCriteria<T>{

		public BuildedCriteria(BuildedCriteria<T> other) {
			super(other);
		}

		public BuildedCriteria(Class<T> type) {
			super(type);
		}

		@Override
		public Criteria<T> duplicate() {
			return new BuildedCriteria<T>(this);
		}

	}

	public Criteria<T> distinct(){
		this.criteria.setDistinct(true);
		return this.criteria;
	}

	public Criteria<T> all(){
		return criteria;
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

	/* (non-Javadoc)
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#isEqual(T)
	 */
	public CriteriaBuilder<T> isEqual(Object instance){
		new BuildingConstraint<T>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), "identity"))
		.eq(instance);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#isSame(T)
	 */
	public CriteriaBuilder<T> isSame(Object instance){
		// TODO read and set unique identifiers
		new BuildingConstraint<T>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), "identity"))
		.eq(instance);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#and(java.lang.String)
	 */
	public Constraint<T,CriteriaBuilder<T>> and(String name) {
		return new BuildingConstraint<T>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), name));
	}

	/* (non-Javadoc)
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#or(java.lang.String)
	 */
	public Constraint<T,CriteriaBuilder<T>> or(String name) {
		//TODO or/and diferentiation
		return new BuildingConstraint<T>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), name));
	}

	public CriteriaBuilder<T>limit(int count) {
		this.criteria.setRange(count);
		return this;
	}

	public CriteriaBuilder<T> limit(int start, int count) {
		this.criteria.setRange(start,count);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#orderBy(java.lang.String)
	 */
	public OrderingConstrain<T,CriteriaBuilder<T>> orderBy(String name) {
		return new BuildingOrdering<T,CriteriaBuilder<T>>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), name));
	}


	void addCriterion(Criterion criterion){
		this.criteria.add(criterion);
	}

	void addOrderingCriterion(OrderingCriterion criterion){
		this.criteria.add(criterion);
	}

	@Override
	protected Class<?> getCurrentType() {
		return criteria.getTargetClass();
	}
}
