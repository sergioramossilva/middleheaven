package org.middleheaven.storage.criteria;



public class JunctionCriteriaBuilder
<Current, Parent , P extends AbstractCriteriaBuilder<Parent, P> > 
extends AbstractCriteriaBuilder<Current, JunctionCriteriaBuilder<Current,Parent,P>>{

	private P parentBuilder;

	protected JunctionCriteriaBuilder(JunctionCriterion criterion, Class<Current> current, P parentBuilder) {
		super(current); // this creates a criteria
		criterion.setSubCriteria(this.criteria);
		this.parentBuilder = parentBuilder;
	}

	/**
	 * Return control to the previous criteria builder
	 * @return
	 */
	public P back(){
		return parentBuilder;
	}




}
