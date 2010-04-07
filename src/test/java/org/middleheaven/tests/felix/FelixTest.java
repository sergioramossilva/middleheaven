package org.middleheaven.tests.felix;

import java.io.File;

import org.junit.Test;
import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.io.repository.ManagedFiles;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

public class FelixTest {

	@Test
	public void testInit() {
		
		
		BootstrapContainer container = new DesktopUIContainer(ManagedFiles.resolveFile(new File(".")));
        StandaloneBootstrap bootstrap = new StandaloneBootstrap(this,container);
        bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));

	}

}
