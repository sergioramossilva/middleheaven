package org.middleheaven.domain.criteria;

import org.middleheaven.util.criteria.JunctionCriterion;

/**
 * Builder used for juntions.
 * @param <Current> the current type
 * @param <Parent> the type that is being joined
 * @param <P> parent builder
 */
public class JunctionCriteriaBuilder <Current, Parent , P extends AbstractEntityCriteriaBuilder<Parent,  P> > 
extends AbstractEntityCriteriaBuilder<Current, JunctionCriteriaBuilder<Current,Parent,P>>{

	private P parentBuilder;

	/**
	 * Constructor.
	 * 
	 * @param criterion the current junction criterion
	 * @param current the current type
	 * @param parentBuilder parent builder
	 */
	protected JunctionCriteriaBuilder(JunctionCriterion criterion, Class<Current> current, P parentBuilder) {
		super(current); // this creates a criteria
		criterion.setSubCriteria(this.criteria);
		this.parentBuilder = parentBuilder;
	}

	/**
	 * Return control to the previous criteria builder.
	 * @return the parent builder
	 */
	public P back(){
		return parentBuilder;
	}




}
