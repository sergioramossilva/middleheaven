package org.middleheaven.ui.models;

import org.middleheaven.ui.components.UITitledUIModel;
import org.middleheaven.ui.events.UIPrespectiveEvent;

public interface UIPrespectiveModel extends UITitledUIModel{

	public void onOpened(UIPrespectiveEvent event);
	public void onClosed(UIPrespectiveEvent event);
	public void onClosing(UIPrespectiveEvent event);

	public void onDeiconified(UIPrespectiveEvent event);
	public void onIconified(UIPrespectiveEvent prespectiveEvent);

}
