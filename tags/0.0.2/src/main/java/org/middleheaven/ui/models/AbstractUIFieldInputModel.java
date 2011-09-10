package org.middleheaven.ui.models;

import org.middleheaven.global.text.Formatter;
import org.middleheaven.ui.AbstractUIModel;

public abstract class AbstractUIFieldInputModel extends AbstractUIModel implements UIFieldInputModel , UINumericInputModel {

	private int maxLength = -1;
	private int minLength = -1;
	private Object value;
	private String name;
	private boolean required;
	
	@Override
	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public int getMinLength() {
		return minLength;
	}

	public void setMaxLength(int maxLength){
		this.maxLength = maxLength;
	}
	
	public void setMinLength(int minLength){
		this.minLength = minLength;
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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean getRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

	
}
