package org.middleheaven.ui.models;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.UIModel;

public interface UITextLabeledModel extends UIModel {

	public TextLocalizable getText();
	public void setText(TextLocalizable text);
	

}
