package org.middleheaven.util.criteria;


public interface FieldCriterion extends Criterion {

	public QualifiedName getFieldName();
	public FieldValueHolder valueHolder();
	public CriterionOperator getOperator();
}
