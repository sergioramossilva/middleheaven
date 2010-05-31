package org.middleheaven.ui.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspWriter;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.rendering.RenderingContext;

public class HtmlUIComponent implements UIComponent {

	private AbstractHtmlRender abstractHTMLRender;
	private UIComponent component;

	public HtmlUIComponent(UIComponent component, AbstractHtmlRender abstractHTMLRender) {
		this.abstractHTMLRender = abstractHTMLRender;
		this.component = component;
	}
	
	public void writeTo(JspWriter writer,RenderingContext context) throws IOException{
		this.abstractHTMLRender.write(writer, context, this.component);
	}

	@Override
	public void addComponent(UIComponent component) {
		// TODO implement HtmlUIComponent.addComponent

	}

	@Override
	public void gainFocus() {
		// TODO implement HtmlUIComponent.gainFocus

	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		// TODO implement HtmlUIComponent.getChildrenComponents
		return null;
	}

	@Override
	public int getChildrenCount() {
		// TODO implement HtmlUIComponent.getChildrenCount
		return 0;
	}

	@Override
	public String getFamily() {
		// TODO implement HtmlUIComponent.getFamily
		return null;
	}

	@Override
	public String getGID() {
		// TODO implement HtmlUIComponent.getGID
		return null;
	}

	@Override
	public <T extends UIComponent> Class<T> getType() {
		// TODO implement HtmlUIComponent.getType
		return null;
	}

	@Override
	public UIModel getUIModel() {
		// TODO implement HtmlUIComponent.getUIModel
		return null;
	}

	@Override
	public UIComponent getUIParent() {
		// TODO implement HtmlUIComponent.getUIParent
		return null;
	}

	@Override
	public boolean hasFocus() {
		// TODO implement HtmlUIComponent.hasFocus
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO implement HtmlUIComponent.isEnabled
		return false;
	}

	@Override
	public boolean isRendered() {
		// TODO implement HtmlUIComponent.isRendered
		return false;
	}

	@Override
	public boolean isVisible() {
		// TODO implement HtmlUIComponent.isVisible
		return false;
	}

	@Override
	public void removeComponent(UIComponent component) {
		// TODO implement HtmlUIComponent.removeComponent

	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO implement HtmlUIComponent.setEnabled

	}

	@Override
	public void setFamily(String family) {
		// TODO implement HtmlUIComponent.setFamily

	}

	@Override
	public void setGID(String id) {
		// TODO implement HtmlUIComponent.setGID

	}

	@Override
	public void setUIModel(UIModel model) {
		// TODO implement HtmlUIComponent.setUIModel

	}

	@Override
	public void setUIParent(UIComponent parent) {
		// TODO implement HtmlUIComponent.setUIParent

	}

	@Override
	public void setVisible(boolean visible) {
		// TODO implement HtmlUIComponent.setVisible

	}

	@Override
	public UIDimension getDimension() {
		// TODO implement HtmlUIComponent.getDimension
		return null;
	}

	@Override
	public int getHeight() {
		// TODO implement HtmlUIComponent.getHeight
		return 0;
	}

	@Override
	public UIPosition getPosition() {
		// TODO implement HtmlUIComponent.getPosition
		return null;
	}

	@Override
	public int getWidth() {
		// TODO implement HtmlUIComponent.getWidth
		return 0;
	}

	@Override
	public int getX() {
		// TODO implement HtmlUIComponent.getX
		return 0;
	}

	@Override
	public int getY() {
		// TODO implement HtmlUIComponent.getY
		return 0;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO implement HtmlUIComponent.setBounds

	}

	@Override
	public void setSize(UIDimension size) {
		// TODO implement HtmlUIComponent.setSize

	}

}
