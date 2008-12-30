package org.middleheaven.ui.models;

import org.middleheaven.ui.UITextLabeledModel;
import org.middleheaven.ui.events.UIActionEvent;

public interface UICommandModel extends UITextLabeledModel{


	public void onCommand(UIActionEvent event);
}
