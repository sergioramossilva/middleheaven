/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

/**
 * 
 */
public interface ConditionTermComparisonOperatorBuilder extends ComparisonOperatorBuilders <ConditionTermComparisonOperatorBuilder , RestrictionTermBuilder>{



	/**
	 * @return
	 */
	WhereTermBuilder endTerm();
	
}
