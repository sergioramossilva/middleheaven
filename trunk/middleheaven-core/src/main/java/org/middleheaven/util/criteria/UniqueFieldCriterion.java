package org.middleheaven.util.criteria;

import org.middleheaven.util.QualifiedName;


public class UniqueFieldCriterion implements FieldCriterion {

	private CriterionOperator operator;
	private FieldValueHolder valueHolder;
	private QualifiedName name;

	public UniqueFieldCriterion(QualifiedName name, CriterionOperator operator,
			FieldValueHolder valueHolder) {
		this.operator = operator;
		this.valueHolder = valueHolder;
		this.name = name;
	}
	
	@Override
	public QualifiedName getFieldName() {
		return name;
	}

	@Override
	public CriterionOperator getOperator() {
		return operator;
	}

	@Override
	public FieldValueHolder valueHolder() {
		return valueHolder;
	}

	@Override
	public boolean isEmpty() {
		return valueHolder.isEmpty();
	}

	@Override
	public Criterion simplify() {
		return this;
	}




}
