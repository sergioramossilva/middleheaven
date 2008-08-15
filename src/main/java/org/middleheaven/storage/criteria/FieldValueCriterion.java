package org.middleheaven.storage.criteria;

import org.middleheaven.data.DataType;
import org.middleheaven.storage.QualifiedName;

class FieldValueCriterion implements FieldCriterion {
	
	private QualifiedName fieldName;
	private DataType dataType;
	private CriterionOperator operator;
	private FieldValueHolder valueHolder;
	

	
	public FieldValueCriterion(QualifiedName fieldName, DataType dataType,
			CriterionOperator operator, FieldValueHolder valueHolder) {

		this.fieldName = fieldName;
		this.dataType = dataType;
		this.operator = operator;
		this.valueHolder = valueHolder;
	}

	@Override
	public QualifiedName getFieldName() {
		return fieldName;
	}

	@Override
	public DataType getFieldType() {
		return dataType;
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
	public Criterion negate() {
		return new FieldValueCriterion(fieldName, dataType,operator.negate(), valueHolder);
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
