package org.middleheaven.logging;

import org.middleheaven.core.services.ServiceRegistry;


public final class Log {

	/**
	 * Acquires a {@link Logger} for a specific log book. 
	 * 
	 * @param bookName the book's name
	 * 
	 * @return a {@link Logger} that will log events on the given book.
	 */
	public static Logger onBook(String bookName){
		
		return new LogServiceDelegatorLogger(bookName, ServiceRegistry.getService(LoggingService.class));
		
	}
	
	public static Logger onBookFor(Class<?> type){
		return onBook(type.getName().toString());
	}
	


}
