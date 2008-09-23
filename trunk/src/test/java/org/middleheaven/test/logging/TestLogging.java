package org.middleheaven.test.logging;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.logging.LoggingLevel;


public class TestLogging {

	@Test
	public void testLoggingLevel(){
		
		assertTrue(LoggingLevel.ALL.canLog(LoggingLevel.ALL));
		assertTrue(LoggingLevel.ALL.canLog(LoggingLevel.TRACE));
		assertFalse(LoggingLevel.TRACE.canLog(LoggingLevel.ALL));
		
		
	}
}
