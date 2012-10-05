/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIMessageModel;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UILabel;

import com.vaadin.ui.Label;

/**
 * 
 */
public class VaadinLabel extends VaadinUIComponent implements UILabel {

	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinLabel() {
		super(new Label(), UILabel.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIReadState getReadState() {
		return UIReadState.OUTPUT_ONLY; 
	}

	public UIMessageModel getUIModel(){
		return (UIMessageModel) super.getUIModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
	
		Label component = (Label) this.getComponent();
		component.setContentMode(Label.CONTENT_TEXT);
		if (this.getUIModel().getText() != null){
			component.setValue(this.localize(this.getUIModel().getText()));
		}
		
	}

}
