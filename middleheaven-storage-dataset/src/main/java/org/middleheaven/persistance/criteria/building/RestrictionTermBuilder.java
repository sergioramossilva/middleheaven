/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface RestrictionTermBuilder {


	/**
	 * @return
	 */
	RestrictionTermBuilder and();

	
	/**
	 * @return
	 */
	RestrictionTermBuilder or();

	/**
	 * @param year
	 * @return
	 */
	<T> ConditionTermComparisonOperatorBuilder column(TypeDefinition<?> definition);


	/**
	 * @return
	 */
	RestrictionTermBuilder endTerm();

	
	ResultColumnSetBuilder endTerms();

	/**
	 * @return
	 */
	RestrictionTermBuilder withTerm();


}
