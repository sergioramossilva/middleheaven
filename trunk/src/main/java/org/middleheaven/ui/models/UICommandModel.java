package org.middleheaven.ui.models;

import org.middleheaven.ui.UITextLabeledModel;

public interface UICommandModel extends UITextLabeledModel{


	public void onCommand(UIActionEvent event);
}
