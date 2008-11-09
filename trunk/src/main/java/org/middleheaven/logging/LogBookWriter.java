
package org.middleheaven.logging;

import java.util.Map;

import org.middleheaven.logging.config.LoggingConfiguration;

/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class LogBookWriter {


    protected LoggingLevel level = LoggingLevel.ALL;


    public LoggingLevel getLevel(){
        return level;
    }

  
    public void setLevel(LoggingLevel level){
        this.level = level;
    }

    public final void log(LoggingEvent event){
        if (this.level.canLog(event.level)){
            write(event);
        }
    }

    public abstract void config(Map<String,String> params, LoggingConfiguration configuration);
    public abstract void write(LoggingEvent event) throws LogWritingException;

}
