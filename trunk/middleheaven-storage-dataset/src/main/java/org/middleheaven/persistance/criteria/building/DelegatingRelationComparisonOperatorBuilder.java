/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.LogicConstraint;

/**
 * 
 */
public class DelegatingRelationComparisonOperatorBuilder extends AbstractComparisonOperatorBuilders<RelationComparisonOperatorBuilder , TargetRelationBuilder> implements
		RelationComparisonOperatorBuilder {

	/**
	 * Constructor.
	 * @param valueLocator
	 * @param builder
	 * @param termBuilder
	 * @param logic
	 */
	public DelegatingRelationComparisonOperatorBuilder(
			ColumnValueLocator valueLocator, DataSetCriteriaBuilder builder, TargetRelationBuilder termBuilder, LogicConstraint logic) {
		super(valueLocator, builder, termBuilder, logic);
	}



}
