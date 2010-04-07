package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.validation.Consistencies;


public abstract class AbstractCriteriaBuilder<T , B extends AbstractCriteriaBuilder<T,B>> {

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
	
	AbstractCriteria<T> criteria;

	protected AbstractCriteriaBuilder(Class<T> type){
		this.criteria = new BuildedCriteria<T>(type);
	}

	private QualifiedName qualify(String name){
		return QualifiedName.qualify(this.getCurrentType().getSimpleName().toLowerCase(), name);
	}
	
	public B hasIdentity(Object identity) {
		Consistencies.consistNotNull(identity);
		
		this.criteria.add(new IdentityCriterion(identity));
		
		return me();
	}

	public B isEqual(Object instance){
		
		Consistencies.consistNotNull(instance);
		
		this.criteria.add(new EqualsOtherInstanceCriterion(instance));
		
		return me();
		
	}


	public B isIdentical(Object instance){
	
		Consistencies.consistNotNull(instance);
		
		this.criteria.add(new IsIdenticalToOtherInstanceCriterion(instance));
		
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


	public Constraint<T,B> or(String name) {
		return new BuildingConstraint<T,B>(me(),qualify(name));
	}
	
	public OrderingConstrain<T,B> orderBy(String name) {
		return new BuildingOrdering<T,B>(me(),qualify(name));
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
