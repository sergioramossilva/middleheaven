/**
 * 
 */
package org.middleheaven.logging;

import org.middleheaven.core.annotations.Component;
import org.middleheaven.core.annotations.Publish;
import org.middleheaven.core.wiring.WiringTarget;

/**
 * Factory of {@link Logger} based on the {@link LoggingService}.
 */
@Component
public class LogBookFactory {
	
	private LoggingService loggingService;

	/**
	 * 
	 * Constructor.
	 * @param loggingService the service being used.
	 */
	public LogBookFactory(LoggingService loggingService){
		this.loggingService = loggingService;
	}

	
	/**
	 * 
	 * @param target used the given {@link WiringTarget} to determine the correct {@link Logger}.
	 * @return the correct {@link Logger} for the {@link WiringTarget}.
	 */
	@Publish
	public Logger getInstance(WiringTarget target){
		return new LogServiceDelegatorLogger(target.getDeclaringType().getName(), this.loggingService);
	}
}
