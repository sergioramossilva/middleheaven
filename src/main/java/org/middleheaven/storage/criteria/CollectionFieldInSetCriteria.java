package org.middleheaven.storage.criteria;

import java.util.Collection;

import org.middleheaven.domain.DataType;
import org.middleheaven.storage.QualifiedName;

public class CollectionFieldInSetCriteria implements FieldInSetCriteria {

	Collection<?> values;
	private QualifiedName name;
	private CriterionOperator operator;
	
	public CollectionFieldInSetCriteria(QualifiedName name , CriterionOperator operator , Collection<?> values ) {
		super();
		this.values = values;
		this.name = name;
		this.operator = operator;
	}
	
	@Override
	public boolean isIncluded() {
		return operator.equals(CriterionOperator.IN);
	}

	@Override
	public boolean useCriteria() {
		return false;
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
		return new SingleObjectValueHolder(values,null);
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Criterion simplify() {
		return this;
	}

	@Override
	public DataType getFieldType() {
		throw new UnsupportedOperationException();
	}



}
