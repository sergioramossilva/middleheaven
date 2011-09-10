package org.middleheaven.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.middleheaven.io.repository.MachineFiles;
import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


public class TestLogging extends MiddleHeavenTestCase {

	@Test
	public void testLoggingLevel(){

		assertTrue(LoggingLevel.ALL.canLog(LoggingLevel.ALL));
		assertTrue(LoggingLevel.ALL.canLog(LoggingLevel.TRACE));
		assertFalse(LoggingLevel.TRACE.canLog(LoggingLevel.ALL));

		assertTrue(LoggingLevel.ALL.canLog(LoggingLevel.INFO));
		assertFalse(LoggingLevel.INFO.canLog(LoggingLevel.ALL));
		assertFalse(LoggingLevel.NONE.canLog(LoggingLevel.INFO));
		assertFalse(LoggingLevel.INFO.canLog(LoggingLevel.DEBUG));
		assertTrue(LoggingLevel.INFO.canLog(LoggingLevel.INFO));
		assertTrue(LoggingLevel.INFO.canLog(LoggingLevel.ERROR));

	}

	@Test
	public void testConfiguration(){
		BasicConfigurator conf = new BasicConfigurator();

		final LoggingConfiguration loggingConfiguration = new LoggingConfiguration( 
				MachineFiles.resolveFile(new File("/"))
		);

		HashLoggingService service = new HashLoggingService(
				loggingConfiguration,
				conf
		);

		LogBook book = service.getLogBook(this.getClass().getName());

		assertNotNull(book);

		final WritableLogBook book2 = new WritableLogBook("org",LoggingLevel.FATAL);
		service.addBook(book2);

		book = service.getLogBook(this.getClass().getName());

		assertNotNull(book);
		assertEquals(book2,book);
	}

}
