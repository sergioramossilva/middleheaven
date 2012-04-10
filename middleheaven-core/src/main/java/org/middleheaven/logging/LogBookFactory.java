/**
 * 
 */
package org.middleheaven.logging;

import org.middleheaven.core.wiring.WiringTarget;
import org.middleheaven.core.wiring.annotations.Component;
import org.middleheaven.core.wiring.annotations.Publish;

/**
 * 
 */
@Component
public class LogBookFactory {
	
	private LoggingService loggingService;

	public LogBookFactory(LoggingService loggingService){
		this.loggingService = loggingService;
	}

	
	@Publish
	public Logger getInstance(WiringTarget target){
		return new LogServiceDelegatorLogger(target.getDeclaringType().getName(), this.loggingService);
	}
}
