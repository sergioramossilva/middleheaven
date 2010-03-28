package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;

public class FieldValueCriterion implements FieldCriterion {
	
	private static final long serialVersionUID = 7204559851317644625L;
	
	private QualifiedName fieldName;
	private CriterionOperator operator;
	private FieldValueHolder valueHolder;
	

	
	public FieldValueCriterion(QualifiedName fieldName,
			CriterionOperator operator, FieldValueHolder valueHolder) {

		this.fieldName = fieldName;
		this.operator = operator;
		this.valueHolder = valueHolder;
	}

	@Override
	public QualifiedName getFieldName() {
		return fieldName;
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
		return false;
	}

	@Override
	public Criterion simplify() {
		return this;
	}
	
	public boolean equals(Object other) {
		return other instanceof FieldValueCriterion
				&& equals((FieldValueCriterion) other);
	}

	public boolean equals(FieldValueCriterion other) {
		return this.fieldName.equals(other.fieldName) && 
				this.operator.equals(other.operator) && 
				this.valueHolder.equalsValue(other.valueHolder)	;
	}

	public int hashCode() {
		return this.fieldName.hashCode();
	}
	
	public String toString(){
		return this.fieldName.toString() + operator.toString() + valueHolder.toString();
		
	}

}
