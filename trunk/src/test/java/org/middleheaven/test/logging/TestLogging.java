package org.middleheaven.test.logging;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.logging.LoggingLevel;
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
}
