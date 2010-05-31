package org.middleheaven.util.criteria.entity;

import org.middleheaven.util.criteria.FieldCriterion;

public interface JunctionCriterion extends FieldCriterion {

	
	public Class<?> getTargetType();
	
	public Class<?> getSourceType();

	public void setAlias(String targetAlias);

	public EntityCriteria<?> getSubCriteria();
	public void setSubCriteria(EntityCriteria<?> criteria);
	
	public String getAlias();

}
