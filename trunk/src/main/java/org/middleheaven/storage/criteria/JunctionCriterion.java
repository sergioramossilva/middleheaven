package org.middleheaven.storage.criteria;

public interface JunctionCriterion extends FieldCriterion {

	
	public Class<?> getTargetType();
	
	public Class<?> getSourceType();

	public void setAlias(String targetAlias);

	public Criteria<?> getSubCriteria();
	public void setSubCriteria(Criteria<?> criteria);
	
	public String getAlias();

}
