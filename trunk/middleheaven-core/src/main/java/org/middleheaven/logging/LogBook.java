/**
 * 
 */
package org.middleheaven.logging;

/**
 * 
 */
public abstract class LogBook  {


    protected final String name;
    protected final LoggingLevel level;

    protected LogBook(String name,LoggingLevel level){
        this.name = name;
        this.level = level;
    }


    public final void log(LoggingEvent event){
    	 if (level.canLog(event.getLevel())){
    		 doLog(event);
         }
    }
    
    protected abstract void doLog(LoggingEvent event);
    


}
