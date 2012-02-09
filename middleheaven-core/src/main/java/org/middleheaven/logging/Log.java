package org.middleheaven.logging;

import org.middleheaven.core.services.ServiceContextUndefinedException;
import org.middleheaven.core.services.ServiceException;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceRegistry;


public class Log {

	/**
	 * Acquires a <code>LogBook</code> with a given name.
	 * If no book is found under that name, a {@code VoidBook} is returned.
	 * If the logging service was not initialized a {@code ConsoleLogBook} is returned
	 * and a message is displayed in {@code Sytem.err}
	 * 
	 * @param name
	 * @return
	 */
	public static LogBook onBook(String bookName){
		try{
			return ServiceRegistry.getService(LoggingService.class).getLogBook(bookName);
		} catch (ServiceNotAvailableException e){
			
			System.err.println("Logging service is unavailable. Using console.");
			return new ConsoleLogBook(LoggingLevel.ALL);
		} catch (ServiceContextUndefinedException e){
			
			System.err.println("Logging service is unavailable. Using console.");
			return new ConsoleLogBook(LoggingLevel.ALL);
		}
	}
	
	public static LogBook onBookFor(Class<?> type){
		return onBook(type.getName().toString());
	}
	


}
