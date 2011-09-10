package org.middleheaven.util.criteria.entity;

import org.middleheaven.util.criteria.FieldCriterion;

/**
 * A criterion that can join two entites.
 */
public interface JunctionCriterion extends FieldCriterion {

	
	public Class<?> getTargetType();
	
	public Class<?> getSourceType();

	public void setAlias(String targetAlias);

	public EntityCriteria<?> getSubCriteria();
	public void setSubCriteria(EntityCriteria<?> criteria);
	
	public String getAlias();
	
	/**
	 * 
	 * @return true if the juction is reversed.
	 */
	public boolean isReversed();

}
