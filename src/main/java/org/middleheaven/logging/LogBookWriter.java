
package org.middleheaven.logging;

import java.text.MessageFormat;
import java.util.Map;

import org.middleheaven.logging.config.LoggingConfiguration;

/**
 * Writes the LoggingEvent on to a book media. 
 */
public abstract class LogBookWriter {

    protected LoggingLevel level = LoggingLevel.ALL;

    protected String formatToText(LoggingEvent event){
    	CharSequence format = event.getMessage();
    	Object [] params = event.getMessageParameters();
    	if (params.length ==0){
    		return format.toString();
    	}
    	final MessageFormat messageFormat = new MessageFormat(format.toString());
    	return messageFormat.format(params);
    }
    
    public LoggingLevel getLevel(){
        return level;
    }

  
    public void setLevel(LoggingLevel level){
        this.level = level;
    }

    public final void log(LoggingEvent event){
        if (this.level.canLog(event.getLevel())){
            write(event);
        }
    }

    public abstract void config(Map<String,String> params, LoggingConfiguration configuration);
    public abstract void write(LoggingEvent event) throws LogWritingException;

}
