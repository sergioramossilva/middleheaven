/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.LogicConstraint;

/**
 * 
 */
public class DelegatingWhereTermComparisonOperatorBuilder extends AbstractComparisonOperatorBuilders<ConditionTermComparisonOperatorBuilder , RestrictionTermBuilder>  implements
		ConditionTermComparisonOperatorBuilder {

	DelegatingTermBuilder termBuilder;

	/**
	 * Constructor.
	 * @param qualifiedName 
	 * @param builder
	 * @param delegatingTermBuilder 
	 * @param logic 
	 */
	public DelegatingWhereTermComparisonOperatorBuilder(
			ColumnValueLocator valueLocator , DataSetCriteriaBuilder builder, DelegatingTermBuilder delegatingTermBuilder, LogicConstraint logic) {
		super(valueLocator, builder, new DelegatingTermBuilder(builder), logic);
		this.termBuilder = delegatingTermBuilder;
	
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public WhereTermBuilder endTerm() {
		return null;
	}



}
