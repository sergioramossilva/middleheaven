package org.middleheaven.logging;


public class ConsoleLogBook extends WritableLogBook {

	public ConsoleLogBook(LoggingLevel level) {
		super("console", level);
		
		this.addWriter(new ConsoleLogWriter());
	}



}
