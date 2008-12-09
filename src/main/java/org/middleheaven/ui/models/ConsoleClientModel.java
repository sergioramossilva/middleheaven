package org.middleheaven.ui.models;

import java.io.Console;

import org.middleheaven.ui.Context;

/**
 * Console based UIClient. Allows to build console applications 
 * at the same time all the power of MiddleHeaven is used being the scenes.
 * 
 * Concrete console clients should implement the <code>main</code> method
 *  
 * @author Sergio Taborda
 */
public abstract class ConsoleClientModel implements UIClientModel {

	@Override
	public final void execute(Context context) {
		
		main(context,System.console());
		
	}
	
	public abstract void main(Context context, Console console);

}
