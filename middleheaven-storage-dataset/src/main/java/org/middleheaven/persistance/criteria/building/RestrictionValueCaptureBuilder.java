/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

/**
 * 
 */
public interface RestrictionValueCaptureBuilder<TERMBUILDER> extends ValueConstraintTermBuilder<TERMBUILDER> {

	/**
	 * @return
	 */
	ValueConstraintTermBuilder<TERMBUILDER> yearIn();

	/**
	 * @return
	 */
	ValueConstraintTermBuilder<TERMBUILDER> dayIn();
	
	/**
	 * @return
	 */
	ValueConstraintTermBuilder<TERMBUILDER> monthIn();
	
	/**
	 * @return
	 */
	ValueConstraintTermBuilder<TERMBUILDER> dateIn();
	
	/**
	 * @return
	 */
	ValueConstraintTermBuilder<TERMBUILDER> timeIn();
	
}
