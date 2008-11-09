package org.middleheaven.tests.felix;

import java.io.File;

import org.junit.Test;
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public class FelixTest {

	@Test
	public void testInit() {
		
		
		Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
        StandaloneBootstrap bootstrap = new StandaloneBootstrap(container);
        bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));

	}

}
