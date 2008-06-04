package org.middleheaven.logging;

import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.config.LoggingConfigurator;
import org.middleheaven.logging.writters.ConsoleLogWriter;

public class BasicConfigurator implements LoggingConfigurator {

	@Override
	public void config(LoggingService configurator, LoggingConfiguration configuration) {
		
		LogBook book = new WritableLogBook(null,LoggingLevel.DEBUG);
		book.addWriter(new ConsoleLogWriter());
		
		configurator.addBook(book);
	}

}
