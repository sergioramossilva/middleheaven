package org.middleheaven.ui.desktop.swing;

import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.UIMessageModel;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.binding.BeanBinding;
import org.middleheaven.ui.components.UILabel;

public class SLabel extends JLabel implements UILabel {


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}
	
	private static final long serialVersionUID = 1L;
	
	private String family;
	private String id;
	private UIMessageModel model;
	private UIComponent parent;

	@Override
	public void setUIModel(UIModel model) {
		this.model = (UIMessageModel)model;
		
		
		this.setText(SDisplayUtils.localize(this.model.getText()));
		
		BeanBinding.bind(this.model, this);
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
		return (Class<T>) UILabel.class;
	}

	@Override
	public UIMessageModel getUIModel() {
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
	public void setDisplayableSize(UISize size) {
		UISize pixelSize =  SwingUnitConverter.getInstance().toPixels(size, this.getUIParent());

		this.setBounds(
				this.getX(), 
				this.getY(), 
				(int)pixelSize.getWidth().getValue(),
				(int)pixelSize.getHeight().getValue()
		);
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
	public UIReadState getReadState() {
		return UIReadState.OUTPUT_ONLY;
	}
	

}
