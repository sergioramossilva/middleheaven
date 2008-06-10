package org.middleheaven.storage.db;

import org.middleheaven.storage.criteria.FieldCriterion;

public interface FieldInSetCriteria extends FieldCriterion{

	boolean isIncluded();

	boolean useCriteria();


}
