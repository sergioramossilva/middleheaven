package org.middleheaven.ui.models;

import org.middleheaven.global.Culture;
import org.middleheaven.global.text.Formatter;
import org.middleheaven.ui.UIModel;

public interface UIOutputModel extends UIModel{

	
	public Object getValue();
	public <T> Formatter<T> getFormater(Culture culture);
}
