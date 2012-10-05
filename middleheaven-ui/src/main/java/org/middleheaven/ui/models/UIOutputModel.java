package org.middleheaven.ui.models;

import org.middleheaven.global.text.Formatter;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UIOutput;


/**
 * A specific {@link UIModel} for the {@link UIOutput} component.
 */
public interface UIOutputModel extends UIModel{

	/**
	 * The value to be outputed.
	 * @return the value to be outputed.
	 */
	public Object getValue();
	
	/**
	 * The formatter, if any, to be used, when displaying the value.
	 * 
	 * @return the formatter to used for the value's display
	 */
	public <T> Formatter<T> getFormater();
}
