/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

/**
 * 
 */
public interface HavingTermComparisonBuilder {


	/**
	 * @return
	 */
	HavingTermComparisonBuilder not();
	
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingTermBuilder> gt();

	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingTermBuilder> lt();
	
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingTermBuilder> ge();
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingTermBuilder> le();
	
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<HavingTermBuilder> eq();

	/**
	 * @return
	 */
	HavingTermBuilder endTerm();

}
