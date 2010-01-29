package org.middleheaven.logging;

import org.middleheaven.core.services.ServiceRegistry;


public class Logging {

	/**
	 * Acquires a <code>LogBook</code> with a given name.
	 * If no book is found under that name, a {@code VoidBook} is returned.
	 * If the logging service was not initialized a {@code ConsoleLogBook} is returned
	 * and a message is displayed in {@code Sytem.err}
	 * 
	 * @param name
	 * @return
	 */
	public static LogBook getBook(String bookName){
//		try{
			return ServiceRegistry.getService(LoggingService.class).getLogBook(bookName);
//		} catch (ServiceNotAvailableException e){
//			
//			System.err.println("Logging service is unavailable. Using console.");
//			return new ConsoleLogBook(LoggingLevel.ALL);
//		}
	}
	
	public static LogBook getBook(Class<?> type){
		return getBook(type.getName().toString());
	}
	
	private static void log(LoggingEvent loggingEvent) {
		// null is the root
		getBook((String)null).log(loggingEvent);
	}

	public static void fatal(Object msg) {
		log(new LoggingEvent(LoggingLevel.FATAL,msg));
	}

	public static void fatal(Object msg, Throwable throwable) {

		log(new LoggingEvent(LoggingLevel.FATAL,msg,throwable));

	}

	public static void error(Object msg) {

		log(new LoggingEvent(LoggingLevel.ERROR,msg));

	}

	public static void error(Object msg, Throwable throwable) {

		log(new LoggingEvent(LoggingLevel.ERROR,msg,throwable));

	}


	public static void warn(Object msg) {

		log(new LoggingEvent(LoggingLevel.WARN,msg));

	}

	public static void warn(Object msg, Throwable throwable) {
		log(new LoggingEvent(LoggingLevel.WARN,msg,throwable));

	}

	public static void info(Object msg) {
		log(new LoggingEvent(LoggingLevel.INFO,msg));

	}

	public static void info(Object msg, Throwable throwable) {
		log(new LoggingEvent(LoggingLevel.INFO,msg,throwable));

	}

	public static void debug(Object msg) {
		log(new LoggingEvent(LoggingLevel.DEBUG,msg));

	}

	public static void debug(Object msg, Throwable throwable) {
		log(new LoggingEvent(LoggingLevel.DEBUG,msg,throwable));

	}

	public static void trace(Object msg) {
		log(new LoggingEvent(LoggingLevel.TRACE,msg));

	}


	public static void trace(Object msg, Throwable throwable) {
		log(new LoggingEvent(LoggingLevel.TRACE,msg,throwable));
	}



}
