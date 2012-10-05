/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIImage;
import org.middleheaven.ui.models.UIImageModel;

import com.vaadin.ui.Label;

/**
 * 
 */
public class VaadinImage extends VaadinUIComponent implements UIImage {

	/**
	 * Constructor.
	 */
	public VaadinImage() {
		super(new Label(), UIImage.class);
	}


	public UIImageModel getUIModel(){
		return (UIImageModel) super.getUIModel();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIReadState getReadState() {
		return UIReadState.OUTPUT_ONLY;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		//no-op
	}

}
