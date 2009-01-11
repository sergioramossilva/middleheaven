package org.middleheaven.ui.models;

public interface UIFieldInputModel extends UIInputModel {

	public void setValue(Object object);
	public int getMaxLength();
	public int getMinLength();
}
