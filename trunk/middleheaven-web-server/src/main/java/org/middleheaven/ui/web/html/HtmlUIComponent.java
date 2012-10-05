package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.rendering.RenderingContext;

public class HtmlUIComponent implements UIComponent , UIContainer , HTMLDocumentWritable {

	protected AbstractHtmlRender abstractHTMLRender;
	private String id;
	private UIModel model;
	private String familly;
	private Class<? extends UIComponent> type;
	private UIComponent parent;
	private List<UIComponent> children = new ArrayList<UIComponent>(0);
	private boolean visible = true;
	private boolean enabled = true;
	private UILayout layout;

	protected HtmlUIComponent (){
		
	}
	public HtmlUIComponent(UIComponent component, AbstractHtmlRender abstractHTMLRender) {
		this.abstractHTMLRender = abstractHTMLRender;
		this.type = component.getComponentType();
		this.model = component.getUIModel();
		this.id = component.getGID();
		this.familly = component.getFamily();
		this.visible = component.isVisible();
		this.enabled = component.isEnabled();
		
	}
	
	public void writeTo(HtmlDocument doc,RenderingContext context) throws IOException{
		this.abstractHTMLRender.write(doc, context, this);
	}

	public String toString(){
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UISize getDisplayableSize() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIPosition getPosition() {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDisplayableSize(UISize size) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGID() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGID(String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRendered() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIModel(UIModel model) {
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIModel getUIModel() {
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) this.type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFamily() {
		return this.familly;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFamily(String familly) {
		this.familly = familly;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent getUIParent() {
		return this.parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIParent(UIComponent parent) {
	   this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildrenCount() {
		return children.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component) {
		children.add(component);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponent(UIComponent component) {
		children.remove(component);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVisible() {
		return visible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout component) {
		this.layout = component;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayout getUIContainerLayout() {
		return this.layout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component, UILayoutConstraint layoutConstrain) {
		
		if (this.layout != null){
			this.layout.addComponent(component, layoutConstrain);
			this.addComponent(component);
		}
	}
	


	

}
