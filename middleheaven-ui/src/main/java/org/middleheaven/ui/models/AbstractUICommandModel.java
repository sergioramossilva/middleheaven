package org.middleheaven.ui.models;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.AbstractUIModel;
import org.middleheaven.ui.UIActionHandler;

public abstract class AbstractUICommandModel extends AbstractUIModel implements UICommandModel {

	TextLocalizable text;
	
	private Collection<UIActionHandler> handlers = new CopyOnWriteArrayList<UIActionHandler>();

	private String name;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Atributes {@link String}.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public TextLocalizable getText() {
		return text;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setText(TextLocalizable text) {
		this.text = firePropertyChange("text", this.text , text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<UIActionHandler> getHandlers() {
		return handlers;
	}
	
	protected void addUIActionHandler(UIActionHandler handler){
		this.handlers.add(handler);
	}
	
}
