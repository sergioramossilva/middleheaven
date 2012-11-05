package org.middleheaven.logging;


public class BasicConfigurator implements LoggingConfigurator {

	@Override
	public void config(ConfigurableLogListener configurator, LoggingConfiguration configuration) {
		
		WritableLogBook book = new WritableLogBook(null,LoggingLevel.DEBUG);
		book.addWriter(new ConsoleLogWriter());
		
		configurator.addBook(book);
	}


}
