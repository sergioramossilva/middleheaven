package org.middleheaven.logging;

import org.middleheaven.core.services.ServiceRegistry;


public class Logging {

	/**
	 * Acquires a <code>LogBook</code> with a given name.
	 * If no book is found under that name, a VoidBook is returned,
	 * this way never corrupting client`s logging code.
	 * @param name
	 * @return
	 */
	public static LogBook getBook(String bookName){
		return ServiceRegistry.getService(LoggingService.class).getLogBook(bookName);
	}
	
	public static LogBook getBook(Class<?> type){
		return getBook(type.getName().toString());
	}
	
	private static void log(LoggingEvent loggingEvent) {
		// null is the root
		getBook((String)null).log(loggingEvent);
	}

	public static void logFatal(Object msg) {
		log(new LoggingEvent(LoggingLevel.FATAL,msg));
	}

	public static void logFatal(Object msg, Throwable throwable) {

		log(new LoggingEvent(LoggingLevel.FATAL,msg,throwable));

	}

	public static void logError(Object msg) {

		log(new LoggingEvent(LoggingLevel.ERROR,msg));

	}

	public static void logError(Object msg, Throwable throwable) {

		log(new LoggingEvent(LoggingLevel.ERROR,msg,throwable));

	}


	public static void logWarn(Object msg) {

		log(new LoggingEvent(LoggingLevel.WARN,msg));

	}

	public static void logWarn(Object msg, Throwable throwable) {
		log(new LoggingEvent(LoggingLevel.WARN,msg,throwable));

	}

	public static void logInfo(Object msg) {
		log(new LoggingEvent(LoggingLevel.INFO,msg));

	}

	public static void logInfo(Object msg, Throwable throwable) {
		log(new LoggingEvent(LoggingLevel.INFO,msg,throwable));

	}

	public static void logDebug(Object msg) {
		log(new LoggingEvent(LoggingLevel.DEBUG,msg));

	}

	public static void logDebug(Object msg, Throwable throwable) {
		log(new LoggingEvent(LoggingLevel.DEBUG,msg,throwable));

	}

	public static void logTrace(Object msg) {
		log(new LoggingEvent(LoggingLevel.TRACE,msg));

	}


	public static void logTrace(Object msg, Throwable throwable) {
		log(new LoggingEvent(LoggingLevel.TRACE,msg,throwable));
	}



}
