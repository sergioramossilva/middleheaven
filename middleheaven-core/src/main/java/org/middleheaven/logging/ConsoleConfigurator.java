package org.middleheaven.logging;


public class ConsoleConfigurator implements LoggingConfigurator {

	
	public ConsoleConfigurator (){}
	
	@Override
	public void config(ConfigurableLogListener configurator, LoggingConfiguration configuration) {
		
		WritableLogBook book = new WritableLogBook(ConfigurableLogListener.DEFAULT_BOOK_NAME,LoggingLevel.DEBUG);
		book.addWriter(new ConsoleLogWriter());
		
		configurator.addBook(book);
	}


}
