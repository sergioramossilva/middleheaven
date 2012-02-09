/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

/**
 * 
 */
public interface HavingColumnBuilder {

	/**
	 * @param string
	 * @return
	 */
	HavingColumnBuilder as(String string);

	/**
	 * @return
	 */
	HavingTermComparisonOperatorBuilder sum();

	/**
	 * @return
	 */
	HavingTermComparisonOperatorBuilder max();
	
	/**
	 * @return
	 */
	HavingTermComparisonOperatorBuilder min();
	
	/**
	 * @return
	 */
	HavingTermComparisonOperatorBuilder avg();
	
	/**
	 * @return
	 */
	HavingTermComparisonOperatorBuilder count();
	
	
	/**
	 * @return
	 */
	HavingTermComparisonOperatorBuilder value();

	/**
	 * @return
	 */
	HavingTermBuilder endExpectation();

}
