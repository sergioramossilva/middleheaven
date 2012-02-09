
package org.middleheaven.logging;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.config.LoggingConfigurator;
import org.middleheaven.logging.writters.ConsoleLogWriter;

/**
 * @author  Sergio M. M. Taborda 
 */
@Service
public class HashLoggingService  implements LoggingService{



    final Map<String, LogBook> books = new HashMap<String, LogBook>();
    LoggingConfiguration configuration ;
    
	public HashLoggingService(LoggingConfiguration configuration , LoggingConfigurator configurator) {
		configurator.config(this,configuration);
		
		this.configuration = configuration;
		
		addBook(new WritableLogBook("",LoggingLevel.ALL).addWriter(new ConsoleLogWriter()));
	}

	@Override
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

    public final LogBook getLogBook(String name) {
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