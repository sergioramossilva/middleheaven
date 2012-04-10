/**
 * 
 */
package org.middleheaven.logging;

import javax.servlet.ServletContext;

/**
 * 
 */
public class ServletContextLogListener implements LoggingEventListener{

	
	private ServletContextLogBookWriter writer;

	public ServletContextLogListener (ServletContext context){
		this.writer = new ServletContextLogBookWriter(context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoggingEvent(LoggingEvent event) {
		
		writer.log(event);
		
	}

	/**
	 * @param valueOf
	 */
	public void setLevel(LoggingLevel level) {
		writer.setLevel(level);
	}

}
