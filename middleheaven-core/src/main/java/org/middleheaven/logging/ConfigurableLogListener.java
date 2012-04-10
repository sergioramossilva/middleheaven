/**
 * 
 */
package org.middleheaven.logging;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.config.LoggingConfigurator;
import org.middleheaven.logging.writters.ConsoleLogWriter;

/**
 * 
 */
public class ConfigurableLogListener implements LoggingEventListener {


    final Map<String, LogBook> books = new HashMap<String, LogBook>();
    LoggingConfiguration configuration ;
    
	public ConfigurableLogListener(LoggingConfiguration configuration , LoggingConfigurator configurator) {
		configurator.config(this,configuration);
		
		this.configuration = configuration;
		
		addBook(new WritableLogBook("",LoggingLevel.ALL).addWriter(new ConsoleLogWriter()));
	}



	public LoggingConfiguration getConfiguration() {
		return configuration;
	}
	
    public void addBook(LogBook book){
        books.put(book.name, book);
    }

    public void removeBook(LogBook book) {
        synchronized (books){
            books.remove(book.name);
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoggingEvent(LoggingEvent event) {
		this.getLogBook(event.getBookName()).log(event);
	}
	
    private final LogBook getLogBook(String name) {
        LogBook book;
        synchronized (books){
             book =  books.get(name);
             while (book==null && name.indexOf('.')>0){
            	 name = name.substring(0, name.lastIndexOf('.'));
            	 book = books.get(name);
             }
        }
        
        if (book == null){
          book =  books.get("");
        }
        
        return book;

    }
}
