package org.middleheaven.logging.config;

import org.middleheaven.logging.ConfigurableLogListener;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.WritableLogBook;
import org.middleheaven.logging.writters.ConsoleLogWriter;

public class BasicConfigurator implements LoggingConfigurator {

	@Override
	public void config(ConfigurableLogListener configurator, LoggingConfiguration configuration) {
		
		WritableLogBook book = new WritableLogBook(null,LoggingLevel.DEBUG);
		book.addWriter(new ConsoleLogWriter());
		
		configurator.addBook(book);
	}


}
