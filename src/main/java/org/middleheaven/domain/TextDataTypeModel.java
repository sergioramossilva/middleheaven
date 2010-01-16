package org.middleheaven.domain;

public class TextDataTypeModel implements DataTypeModel{

	private Integer minLength;
	private Integer maxLength;
	
	public TextDataTypeModel(){}
	
	@Override
	public DataType getDataType() {
		return DataType.TEXT;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	
}
