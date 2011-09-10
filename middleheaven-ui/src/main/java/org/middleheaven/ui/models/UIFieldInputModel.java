package org.middleheaven.ui.models;

public interface UIFieldInputModel extends UIInputModel {

	public void setValue(Object object);
	public int getMaxLength();
	public int getMinLength();
	public void setRequired(boolean required);
	public boolean getRequired();
	public void setMaxLength(int maxLength);
	public void setMinLength(int minLength);
}
