/*
 * Created on 2006/11/25
 *
 */
package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.io.repository.MachineFiles;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

/**
 * Entry Point for desktop applications.
 * A desktop application may extend DesktopStarter 
 * and redirected the call to standard main to execute()
 * 
 *
 */
public abstract class DesktopStarter {

	
	
    public void execute(String[] args){
    	
    	BootstrapContainer container = new DesktopUIContainer(MachineFiles.getDefaultFolder());
        DesktopBootstrap bootstrap = new DesktopBootstrap(this,container);
        bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
       
    }
    
   
}
