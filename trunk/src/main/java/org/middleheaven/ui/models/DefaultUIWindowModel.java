package org.middleheaven.ui.models;

import org.middleheaven.ui.AbstractUIModel;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UITreeCriteria;
import org.middleheaven.ui.events.UIPrespectiveEvent;
import org.middleheaven.ui.events.UIWindowEvent;

public class DefaultUIWindowModel extends AbstractUIModel implements UIWindowModel {

	private String title;
	
	@Override
	public void onClosed(UIPrespectiveEvent event) {
		UIClient client =UITreeCriteria.search("/").execute(event.getSource()).first(UIClient.class);
		client.exit();
	}

	@Override
	public void onClosing(UIPrespectiveEvent event) {
		event.getSource().setVisible(false);
	}

	@Override
	public void onDeiconified(UIPrespectiveEvent event) {
		//no-op
	}

	@Override
	public void onIconified(UIPrespectiveEvent prespectiveEvent) {
		//no-op
	}

	@Override
	public void onOpened(UIPrespectiveEvent event) {
		//no-op
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void onAtivated(UIWindowEvent event) {
		// no-op
	}

	@Override
	public void onDeativated(UIWindowEvent event) {
		// no-op
	}

}
