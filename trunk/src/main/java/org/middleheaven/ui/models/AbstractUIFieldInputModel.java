package org.middleheaven.ui.models;

import org.middleheaven.global.text.Formatter;
import org.middleheaven.ui.AbstractUIModel;

public class AbstractUIFieldInputModel extends AbstractUIModel implements UIFieldInputModel , UINumericInputModel {

	int maxLength = -1;
	int minLength = -1;
	Object value;
	
	@Override
	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public int getMinLength() {
		return minLength;
	}

	@Override
	public void setValue(Object newValue) {
		Object oldValue = this.value;
		this.value = newValue;
		firePropertyChange("value", oldValue, newValue);
	
	}

	@Override
	public <T> Formatter<T> getFormater() {
		// TODO implement AbstractUIFieldInputModel.getFormater
		return null;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public int getDecimalDigits() {
		return 0;
	}

	
}
