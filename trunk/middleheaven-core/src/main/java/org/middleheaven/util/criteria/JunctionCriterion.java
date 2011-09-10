package org.middleheaven.util.criteria;


/**
 * A criterion that can join two entites.
 */
public interface JunctionCriterion extends FieldCriterion {

	
	public Class<?> getTargetType();
	
	public Class<?> getSourceType();

	public void setAlias(String targetAlias);

	public Criteria<?> getSubCriteria();
	public void setSubCriteria(Criteria<?> criteria);
	
	public String getAlias();
	
	/**
	 * 
	 * @return true if the juction is reversed.
	 */
	public boolean isReversed();

}
