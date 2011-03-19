package org.middleheaven.domain.model;

/**
 * Text Data type.
 */
public class TextDataTypeModel implements DataTypeModel{

	private Integer minLength;
	private Integer maxLength;
	private boolean emptyable = true;
	private boolean isEmail = false;
	private boolean isUrl = false;
	
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
	
	public boolean isEmptyable(){
		return emptyable;
	}
	
	public void setEmptyable(boolean emptyable){
		this.emptyable = emptyable;
	}

	public boolean isEmailAddress() {
		return isEmail;
	}

	public void setEmailAddress(boolean isEmail) {
		this.isEmail = isEmail;
	}

	public boolean isUrl() {
		return isUrl;
	}

	public void setUrl(boolean isUrl) {
		this.isUrl = isUrl;
	}



	
}
