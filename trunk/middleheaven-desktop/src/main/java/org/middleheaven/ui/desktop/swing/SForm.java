package org.middleheaven.ui.desktop.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.ui.NamingContainer;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.UIFormModel;

/**
 * Form implementation in swing. 
 */
public class SForm extends SBasePanel implements UIForm ,NamingContainer {

	UICommandSet commandSet;
	UILayout layout;
	Map<String, UIComponent> componets = new TreeMap<String,UIComponent>(); 
	
	public SForm(UICommandSet commandSet, UILayout layout){
		this.setLayout(new BorderLayout());
		
		this.commandSet = commandSet;
		this.layout = layout;
		
		componets.put(layout.getGID(), layout);
		componets.put(commandSet.getGID(), commandSet);
		
		commandSet.setUIParent(this);
		layout.setUIParent(this);
		
		this.add((Component)commandSet, BorderLayout.NORTH);
		this.add((Component)layout, BorderLayout.CENTER);
	}
	
	public UIFormModel getUIModel() {
		return (UIFormModel) super.getUIModel();
	}
	
	@Override
	public UIComponent findContainedComponent(String componentID) {
		return this.componets.get(componentID);
	}
	
	@Override
	public UICommandSet getCommandSet() {
		return commandSet;
	}

	@Override
	public UILayout getUIContainerLayout() {
		return layout;
	}

	@Override
	public void setUIContainerLayout(UILayout component) {
		this.layout = component;
	}
	
	@Override
	public void addComponent(UIComponent component) {
		this.componets.put(component.getGID(), component);
		
		if (UICommand.class.isAssignableFrom(component.getComponentType())){
			this.commandSet.addComponent(component);
		} else {
			this.layout.addComponent(component);
		}
	}
	
	@Override
	public void addComponent(UIComponent component,UILayoutConstraint layoutConstrain) {
		this.componets.put(component.getGID(), component);
		
		if (UICommand.class.isAssignableFrom(component.getComponentType())){
			this.commandSet.addComponent(component);
		} else {
			this.layout.addComponent(component, layoutConstrain);
		}
	}



	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIForm.class;
	}



}
