package org.middleheaven.storage.db;

import org.middleheaven.domain.DataType;
import org.middleheaven.storage.criteria.CriterionOperator;
import org.middleheaven.storage.criteria.FieldValueHolder;


public class ColumnValueHolder {

	Object value;
	DataType dataType;
	CriterionOperator op  = CriterionOperator.EQUAL;

	public ColumnValueHolder(Object value, DataType dataType) {
		this.value = value;
		this.dataType = dataType;
	}

	public ColumnValueHolder(FieldValueHolder vholder, CriterionOperator op) {
		this.value = vholder.getValue();
		this.dataType = vholder.getDataType();
		this.op = op;
	}

	public Object getValue() {
		return value;
	}

	public DataType getDataType() {
		return dataType;
	}

	protected CriterionOperator getOperator() {
		return op;
	}

}
