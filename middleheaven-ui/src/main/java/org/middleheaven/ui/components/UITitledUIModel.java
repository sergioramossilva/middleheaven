package org.middleheaven.ui.components;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.UIModel;

public interface UITitledUIModel extends UIModel {

	
	public TextLocalizable getTitle();
	public void setTitle(TextLocalizable title);
}
