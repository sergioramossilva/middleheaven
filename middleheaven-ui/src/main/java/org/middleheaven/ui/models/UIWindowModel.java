package org.middleheaven.ui.models;

import org.middleheaven.ui.events.UIWindowEvent;


public interface UIWindowModel extends UIPrespectiveModel {

	public void onAtivated(UIWindowEvent event);
	public void onDeativated(UIWindowEvent event);

}
