
package org.middleheaven.logging;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.config.LoggingConfigurator;

/**
 * @author  Sergio M. M. Taborda 
 */
public class HashLoggingService  implements LoggingService{


    static final VoidLogBook VOID_BOOK = VoidLogBook.getInstance();
    final Map<String, LogBook> books = new HashMap<String, LogBook>();
    LoggingConfiguration configuration ;
    
	public HashLoggingService(LoggingConfiguration configuration , LoggingConfigurator configurator) {
		configurator.config(this,configuration);
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
            books.remove(book);
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
        	book=VOID_BOOK;
        	books.put(name, book); // cache for next search
        }
        
        return book;

    }





}