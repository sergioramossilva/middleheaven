package org.middleheaven.storage.criteria;


public class ReferenceCriteriaBuilder<Current, Parent , P extends CriteriaBuilderStrategy<Parent, P> > 
	extends CriteriaBuilderStrategy<Current , ReferenceCriteriaBuilder<Current, Parent , P > >{

	private P parentBuilder;
	private Class<Current> currentType;

	protected ReferenceCriteriaBuilder(Class<Current> current, P parentBuilder) {
		this.currentType = current;
		this.parentBuilder = parentBuilder;
	}
	
	/**
	 * Return control to the previous criteria builder
	 * @return
	 */
	public P back(){
		return parentBuilder;
	}

	@Override
	public Constraint<Current, ReferenceCriteriaBuilder<Current, Parent, P>> and(String name) {
		// TODO implement CriteriaBuilderStrategy<Current,ReferenceCriteriaBuilder<Current,Parent,P>>.and
		return null;
	}

	@Override
	public ReferenceCriteriaBuilder<Current, Parent, P> isEqual(Object instance) {
		// TODO implement CriteriaBuilderStrategy<Current,ReferenceCriteriaBuilder<Current,Parent,P>>.isEqual
		return null;
	}

	@Override
	public ReferenceCriteriaBuilder<Current, Parent, P> isSame(Object instance) {
		// TODO implement CriteriaBuilderStrategy<Current,ReferenceCriteriaBuilder<Current,Parent,P>>.isSame
		return null;
	}

	@Override
	public Constraint<Current, ReferenceCriteriaBuilder<Current, Parent, P>> or(String name) {
		// TODO implement CriteriaBuilderStrategy<Current,ReferenceCriteriaBuilder<Current,Parent,P>>.or
		return null;
	}



	@Override
	void addCriterion(Criterion criterion) {
		this.parentBuilder.addCriterion(criterion);
	}

	@Override
	void addOrderingCriterion(OrderingCriterion criterion) {
		this.parentBuilder.addOrderingCriterion(criterion);
	}

	@Override
	protected Class<?> getCurrentType() {
		return this.currentType;
	}

	@Override
	public OrderingConstrain<Current, ReferenceCriteriaBuilder<Current, Parent, P>> orderBy(
			String name) {
		// TODO implement CriteriaBuilderStrategy<Current,ReferenceCriteriaBuilder<Current,Parent,P>>.orderBy
		return null;
	}

	
	
}
