package org.middleheaven.util.criteria;


public abstract class AbstractCriteriaBuilder<T , B extends AbstractCriteriaBuilder<T,B>> {

	
	protected abstract void addOrderingCriterion(OrderingCriterion criterion);
	protected abstract void addCriterion(Criterion criterion);
}
