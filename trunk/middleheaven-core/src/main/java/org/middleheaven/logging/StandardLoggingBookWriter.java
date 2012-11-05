/**
 * 
 */
package org.middleheaven.logging;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


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
			break;
		case DEBUG:
			level = Level.FINE;
			break;
		case INFO:
			level = Level.INFO;
			break;
		case WARN:
			level = Level.WARNING;
			break;
		case ERROR:
		case FATAL:
			level = Level.SEVERE;
			break;
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
