/**
 * 
 */
package org.middleheaven.logging;

/**
 * A listener for {@link LoggingEvent}s.
 * 
 * The {@link LoggingEventListener} should be registred with {@link LoggingService} in order to receiving events.
 */
public interface LoggingEventListener {

	/**
	 * Receive an event.
	 * 
	 * @param event the received event.
	 */
	public void onLoggingEvent(LoggingEvent event);
}
