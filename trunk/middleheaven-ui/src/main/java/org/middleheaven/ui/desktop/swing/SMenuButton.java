package org.middleheaven.ui.desktop.swing;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import org.middleheaven.core.reflection.bean.BeanBinding;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.ui.events.UIActionEvent;
import org.middleheaven.ui.models.UICommandModel;

public class SMenuButton extends JMenuItem implements UICommand {

	private static final long serialVersionUID = 1L;
	
	private String family;
	private String id;
	private UICommandModel model;
	private UIComponent parent;

	public SMenuButton(){
		super ();
		
		this.setAction(new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				model.onCommand(new UIActionEvent(SMenuButton.this));
			}
			
		});
	}
	
	@Override
	public void setUIModel(UIModel model) {
		this.model = (UICommandModel)model;
		this.setText(this.model.getText());
		
		BeanBinding.bind(this.model, this.getAction());
	}
	
	@Override
	public void addComponent(UIComponent component) {
		//no-op
	}

	@Override
	public void gainFocus() {
		//no-op
	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.emptyList();
	}

	@Override
	public int getChildrenCount() {
		return 0;
	}

	@Override
	public String getFamily() {
		return family;
	}

	@Override
	public String getGID() {
		return id;
	}

	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UILabel.class;
	}

	@Override
	public UICommandModel getUIModel() {
		return model;
	}

	@Override
	public UIComponent getUIParent() {
		return parent;
	}

	@Override
	public boolean isRendered() {
		return true;
	}

	@Override
	public void removeComponent(UIComponent component) {
		// nop-op
	}

	@Override
	public void setFamily(String family) {
		this.family = family;
	}

	@Override
	public void setGID(String id) {
		this.id= id;
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public UIDimension getDimension() {
		return new UIDimension(this.getWidth(), this.getHeight());
	}

	@Override
	public UIPosition getPosition() {
		return new UIPosition(this.getX(),this.getY());
	}

	@Override
	public void setSize(UIDimension size) {
		this.setBounds(this.getX(), this.getY(), size.getWidth(), size.getHeight());
	}



}
