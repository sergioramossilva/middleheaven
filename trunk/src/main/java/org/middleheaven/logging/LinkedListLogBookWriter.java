package org.middleheaven.logging;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.logging.config.LoggingConfiguration;

public final class LinkedListLogBookWriter extends LogBookWriter{

	private final LinkedList<LoggingEvent> events = new LinkedList<LoggingEvent>();
	
	/**
	 * No configuration is needed
	 */
	@Override
	public void config(Map<String, String> params, LoggingConfiguration configuration) {
		//no-op
	}

	@Override
	public void write(LoggingEvent event) throws LogWritingException {
		events.add(event);
	}
	
	public List<LoggingEvent> getEvents(){
		return events;
	}
	
	public void writeTo(LogBookWriter other){
		for (LoggingEvent event: events){
			other.write(event);
		}
	}

	public void writeTo(LogBook book){
		for (LoggingEvent event: events){
			book.log(event);
		}
	}
}
