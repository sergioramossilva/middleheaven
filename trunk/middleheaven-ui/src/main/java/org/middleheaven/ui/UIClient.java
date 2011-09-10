package org.middleheaven.ui;

import org.middleheaven.ui.models.UIClientModel;



/**
 * The client graphical interface the user interacts with in order
 * to input commands and receive informations.
 * 
 */
public interface UIClient extends UIComponent{


	public UIClientModel getUIModel();

	public void exit();
	
	
}
