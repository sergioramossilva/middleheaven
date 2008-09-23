package org.middleheaven.ui.client;

import org.middleheaven.ui.Context;

/**
 * The client graphical interface the user interact with in order
 * to input commands and receive informations.
 * 
 * @author Sergio Taborda
 */
public interface UIClient {

	/**
	 * Start the client UI.
	 * @param context context with informations
	 */
	public void execute(Context context);
}
