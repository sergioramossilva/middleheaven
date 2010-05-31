package org.middleheaven.ui.web;

import java.beans.PropertyChangeListener;

import org.middleheaven.ui.events.UIActionEvent;
import org.middleheaven.ui.events.UIFocusEvent;
import org.middleheaven.ui.models.UICommandModel;

public class HtmlComandButtonModel implements UICommandModel {

	private String text;
	private boolean enabled;
	
	@Override
	public void onCommand(UIActionEvent event) {
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO implement HtmlComandButtonModel.addPropertyChangeListener

	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void onFocusGained(UIFocusEvent event) {
		// TODO implement HtmlComandButtonModel.onFocusGained

	}

	@Override
	public void onFocusLost(UIFocusEvent event) {
		// TODO implement HtmlComandButtonModel.onFocusLost

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO implement HtmlComandButtonModel.removePropertyChangeListener

	}



}
