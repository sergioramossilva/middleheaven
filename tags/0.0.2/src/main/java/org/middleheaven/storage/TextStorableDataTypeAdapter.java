package org.middleheaven.storage;

import org.middleheaven.model.domain.DataType;
import org.middleheaven.model.domain.TextDataTypeModel;

public class TextStorableDataTypeAdapter extends TextDataTypeModel implements StorableDataTypeModel {

	
	private TextDataTypeModel original;

	public TextStorableDataTypeAdapter (TextDataTypeModel original){
		this.original = original;
	}
	
	public DataType getDataType() {
		return original.getDataType();
	}

	public Integer getMaxLength() {
		return original.getMaxLength();
	}

	public Integer getMinLength() {
		return original.getMinLength();
	}

	public int hashCode() {
		return original.hashCode();
	}

	public boolean isEmailAddress() {
		return original.isEmailAddress();
	}

	public boolean isEmptyable() {
		return original.isEmptyable();
	}

	public boolean isUrl() {
		return original.isUrl();
	}

	public void setEmailAddress(boolean isEmail) {
		original.setEmailAddress(isEmail);
	}

	public void setEmptyable(boolean emptyable) {
		original.setEmptyable(emptyable);
	}

	public void setMaxLength(Integer maxLength) {
		original.setMaxLength(maxLength);
	}

	public void setMinLength(Integer minLength) {
		original.setMinLength(minLength);
	}

	public void setUrl(boolean isUrl) {
		original.setUrl(isUrl);
	}

	public String toString() {
		return original.toString();
	}

	

	
}
