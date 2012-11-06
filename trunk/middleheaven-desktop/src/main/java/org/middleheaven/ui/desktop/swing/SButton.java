package org.middleheaven.ui.desktop.swing;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.middleheaven.ui.UIActionHandlerLocator;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.binding.BeanBinding;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.desktop.DesktopContext;
import org.middleheaven.ui.models.UICommandModel;

public class SButton extends JButton implements UICommand {

	private static final long serialVersionUID = 1L;
	
	private String family;
	private String id;
	private UICommandModel model;
	private UIComponent parent;
	
	public SButton(){
		super ();
		
		this.setAction(new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				UIActionHandlerLocator.getLocator(DesktopContext.getDesktopContext().getAttributeContext()).handle(model.getName()).from(SButton.this);
				
			}
			
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}
	
	
	@Override
	public void setUIModel(UIModel model) {
		this.model = (UICommandModel)model;
		
		this.setText(SDisplayUtils.localize(this.model.getText()));
		
		BeanBinding.bind(this.model, this.getAction());
	}

	
	@Override
	public void addComponent(UIComponent component) {
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
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UICommand.class;
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
	public UISize getDisplayableSize() {
		return UISize.pixels(this.getWidth(), this.getHeight());
	}

	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(this.getX(),this.getY());
	}

	@Override
	public void setDisplayableSize(UISize size) {
		UISize pixelSize =  SwingUnitConverter.getInstance().toPixels(size, this.getUIParent());

		this.setBounds(
				this.getX(), 
				this.getY(), 
				(int)pixelSize.getWidth().getValue(),
				(int)pixelSize.getHeight().getValue()
		);
	}

}
