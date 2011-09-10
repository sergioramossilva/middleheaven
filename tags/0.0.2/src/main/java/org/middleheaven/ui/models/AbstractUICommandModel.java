package org.middleheaven.ui.models;

import org.middleheaven.ui.AbstractUIModel;

public abstract class AbstractUICommandModel extends AbstractUIModel implements UICommandModel {

	String text;
	
	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = firePropertyChange("text", this.text , text);
	}


}
