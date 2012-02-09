/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

/**
 * 
 */
public interface ComparisonOperatorBuilders <SELF , TERMBUILDER > {

	
	/**
	 * @return
	 */
	SELF not();
	
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<TERMBUILDER> gt();

	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<TERMBUILDER> lt();
	
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<TERMBUILDER> ge();
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<TERMBUILDER> le();
	
	/**
	 * @return
	 */
	RestrictionValueCaptureBuilder<TERMBUILDER> eq();
}
