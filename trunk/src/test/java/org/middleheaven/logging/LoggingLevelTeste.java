package org.middleheaven.logging;

import static org.junit.Assert.*;

import org.junit.Test;


public class LoggingLevelTeste {

	
	@Test
	public void testLoggingLevel(){

		assertTrue(LoggingLevel.ALL.canLog(LoggingLevel.INFO));
		assertFalse(LoggingLevel.INFO.canLog(LoggingLevel.ALL));
		assertFalse(LoggingLevel.NONE.canLog(LoggingLevel.INFO));
		assertFalse(LoggingLevel.INFO.canLog(LoggingLevel.DEBUG));
		assertTrue(LoggingLevel.INFO.canLog(LoggingLevel.INFO));
		assertTrue(LoggingLevel.INFO.canLog(LoggingLevel.ERROR));
	}
}
