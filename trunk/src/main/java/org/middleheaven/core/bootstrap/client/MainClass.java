/*
 * Created on 2006/11/25
 *
 */
package org.middleheaven.core.bootstrap.client;

import java.io.File;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public class MainClass {

    public void execute(String[] args){
    	
    	Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
        StandaloneBootstrap bootstrap = new StandaloneBootstrap(container);
        bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
    }
}
