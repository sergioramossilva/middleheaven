package org.middleheaven.ui.models;

import org.middleheaven.ui.Context;
import org.middleheaven.ui.UIModel;

public interface UIClientModel extends UIModel {

	
	/**
	 * Start the client UI.
	 * @param context context with informations
	 */
	public void execute(Context context);
	
}
