package org.middleheaven.storage.criteria;

import org.middleheaven.data.DataType;
import org.middleheaven.storage.QualifiedName;

public interface FieldCriterion extends Criterion {

	public QualifiedName getFieldName();
	public DataType getFieldType();
	public FieldValueHolder valueHolder();
	public CriterionOperator getOperator();
}
