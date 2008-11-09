
package org.middleheaven.logging;

import java.util.Map;

import javax.servlet.ServletContext;

import org.middleheaven.logging.config.LoggingConfiguration;

public class ServletContextLogBookWriter extends LogBookWriter {

	private ServletContext context;
	
	public ServletContextLogBookWriter(ServletContext context) {
		this.context = context;
	}

	@Override
	public void config(Map<String, String> params, LoggingConfiguration configuration) {
	}

	@Override
	public void write(LoggingEvent event) throws LogWritingException {
		if (event.t==null){
			context.log(event.msg.toString());
		} else {
			context.log(event.msg.toString(),event.t);
		}
	}

}
