/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

/**
 * 
 */
public interface HavingTermComparisonOperatorBuilder {

	/**
	 * @return
	 */
	HavingTermComparisonOperatorBuilder not();
	
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingColumnBuilder> gt();

	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingColumnBuilder> lt();
	
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingColumnBuilder> ge();
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingColumnBuilder> le();
	
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingColumnBuilder> eq();

	/**
	 * @return
	 */
	HavingTermBuilder endExpectation();

	/**
	 * @return
	 */
	DataSetCriteriaBuilder endExpectations();
	
	
}
