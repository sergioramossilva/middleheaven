package org.middleheaven.storage.criteria;

import org.middleheaven.domain.DataType;
import org.middleheaven.storage.QualifiedName;

public interface FieldCriterion extends Criterion {

	public QualifiedName getFieldName();
	public FieldValueHolder valueHolder();
	public CriterionOperator getOperator();
}
