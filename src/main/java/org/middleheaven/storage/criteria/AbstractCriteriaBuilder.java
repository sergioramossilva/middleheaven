package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.validation.Consistencies;


public abstract class AbstractCriteriaBuilder<T , B extends AbstractCriteriaBuilder<T,B>> {

	AbstractCriteria<T> criteria;

	protected AbstractCriteriaBuilder(Class<T> type){
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
	
	private QualifiedName qualify(String name){
		return QualifiedName.qualify(this.getCurrentType().getSimpleName().toLowerCase(), name);
	}
	/**
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#isEqual(T)
	 */
	public B isEqual(Object instance){
		// TODO read and set unique identifiers
		new BuildingConstraint<T,B>(me(),qualify("identity"))
		.eq(instance);
		return me();
	}

	/**
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#isSame(T)
	 */
	public B isIdentical(Object instance){
	
		new BuildingConstraint<T,B>(me(),qualify("identity"))
		.is(instance);
		return me();
	}

	/**
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#and(java.lang.String)
	 */
	public Constraint<T,B> and(String name) {
		return new BuildingConstraint<T,B>(me(),qualify(name));
	}

	private B me(){
		return (B) this;
	}
	
	/**
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#or(java.lang.String)
	 */
	public Constraint<T,B> or(String name) {
		return new BuildingConstraint<T,B>(me(),qualify(name));
	}
	
	/* (non-Javadoc)
	 * @see org.middleheaven.storage.criteria.CriteriaBuilderStrategy#orderBy(java.lang.String)
	 */
	public OrderingConstrain<T,B> orderBy(String name) {
		return new BuildingOrdering<T,B>(me(),qualify(name));
	}

	public B hasIdentity(Object identity) {
		Consistencies.consistNotNull(identity);
		
		this.criteria.add(new IdentityCriterion(identity));
		
		return me();
	}

	
	void addCriterion(Criterion criterion){
		this.criteria.add(criterion);
	}

	void addOrderingCriterion(OrderingCriterion criterion){
		this.criteria.add(criterion);
	}
	protected Class<T> getCurrentType(){
		return this.criteria.getTargetClass();
	}
}
