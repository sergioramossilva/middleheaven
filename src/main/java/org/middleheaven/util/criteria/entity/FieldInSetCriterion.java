package org.middleheaven.util.criteria.entity;

import org.middleheaven.util.criteria.FieldCriterion;


public interface FieldInSetCriterion extends FieldCriterion{

	boolean isIncluded();

	boolean useCriteria();


}
