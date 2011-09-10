/**
 * 
 */
package org.middleheaven.logging;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.middleheaven.logging.config.LoggingConfiguration;

/**
 * 
 */
public class StandardLoggingBookWriter extends LogBookWriter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void config(Map<String, String> params, LoggingConfiguration configuration) {
		// no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(LoggingEvent event) throws LogWritingException {
		Logger log = Logger.getLogger(event.getBookName());

		// Mapping
		//FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE
		//TRACE ,  - , DEBUG,  - , INFO , WARN , FATAL + ERROR
		
		Level level;
		switch (event.getLevel()){
		case TRACE:
			level = Level.FINEST;
		case DEBUG:
			level = Level.FINE;
		case INFO:
			level = Level.INFO;
		case WARN:
			level = Level.WARNING;
		case ERROR:
		case FATAL:
			level = Level.SEVERE;
		case NONE:
		case ALL:
		default:
			level = Level.OFF;
		}
		
		if (event.getThrowable() != null){
			log.log(level, event.getMessage().toString() , event.getThrowable());
		} else {
			log.log(level, event.getMessage().toString() , event.getMessageParameters());
		}
		
		
	}



}
