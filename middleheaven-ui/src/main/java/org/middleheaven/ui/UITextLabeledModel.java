package org.middleheaven.ui;

import org.middleheaven.global.text.TextLocalizable;

public interface UITextLabeledModel extends UIModel {

	public TextLocalizable getText();
	public void setText(TextLocalizable text);
	

}
