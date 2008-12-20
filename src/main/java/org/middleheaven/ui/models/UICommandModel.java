package org.middleheaven.ui.models;

import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UICommand;

public interface UICommandModel extends UIModel{

	public String getText();
	public void setText(String text);
	
	public boolean isEnabled();
	public void setEnabled(boolean enabled);
	
	public void onCommand(UICommand command);
}
