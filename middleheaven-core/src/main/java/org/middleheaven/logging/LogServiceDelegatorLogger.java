/**
 * 
 */
package org.middleheaven.logging;

/**
 * 
 */
public final class LogServiceDelegatorLogger extends Logger {

	private LoggingService loggingService;

	/**
	 * Constructor.
	 * @param name
	 * @param level
	 */
	public LogServiceDelegatorLogger(String bookName, LoggingService loggingService) {
		super(bookName);
		
		this.loggingService = loggingService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doLog(LoggingEvent event) {
		loggingService.log(event);
	}
	
	

}
