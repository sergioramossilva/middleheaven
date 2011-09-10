package org.middleheaven.logging;

import org.middleheaven.logging.writters.ConsoleLogWriter;

public class ConsoleLogBook extends WritableLogBook {

	public ConsoleLogBook(LoggingLevel level) {
		super("console", level);
		
		this.addWriter(new ConsoleLogWriter());
	}



}
