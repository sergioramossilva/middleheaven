package org.middleheaven.tests.desktop;

import org.junit.Test;
import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.client.DesktopBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.io.repository.MachineFiles;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public class DesktopUIContainerTest {

	@Test
	public void testInit() {
		
		
		BootstrapContainer container = new DesktopUIContainer(MachineFiles.getDefaultFolder());
        DesktopBootstrap bootstrap = new DesktopBootstrap(this,container);
        bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));

	}

}
