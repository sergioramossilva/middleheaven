
package org.middleheaven.logging;




/**
 * Represents a repository where LogEvents are registered.
 * 
 */
public abstract class LogBook {

    protected final String name;
    protected final LoggingLevel level;

    protected LogBook(String name,LoggingLevel level){
        this.name = name;
        this.level = level;
    }

    public final void fatal(CharSequence msg, Object ... params) {
        fatal(null,msg,params);
    }

    public void fatal(Throwable throwable, CharSequence message,Object ... params) {
        if (level.canLog(LoggingLevel.FATAL)){
            log(new LoggingEvent(name, LoggingLevel.FATAL,message,throwable,params));
        }
    }
    
    public final void error(CharSequence msg, Object ... params) {
        error(null,msg,params);
    }

    public void error(Throwable throwable, CharSequence message,Object ... params) {
        if (level.canLog(LoggingLevel.ERROR)){
            log(new LoggingEvent(name, LoggingLevel.ERROR,message,throwable,params));
        }
    }
   
    public final void warn(CharSequence msg, Object ... params) {
        warn(null,msg,params);
    }
    
    public void warn(Throwable throwable,CharSequence msg, Object ... params) {
        if (level.canLog(LoggingLevel.WARN)){
            log(new LoggingEvent(name,LoggingLevel.WARN,msg,throwable, params));
        }
    }
    
    public void info(CharSequence msg, Object ... params) {
        info(null,msg,params);
    }
    
    public void info(Throwable throwable,CharSequence msg, Object ... params) {
        if (level.canLog(LoggingLevel.INFO)){
            log(new LoggingEvent(name,LoggingLevel.INFO,msg,throwable, params));
        }
    }

    public final void debug(CharSequence msg, Object ... params) {
    	debug(null,msg,params);
    }
    
    public void debug(Throwable throwable,CharSequence msg, Object ... params) {
        if (level.canLog(LoggingLevel.DEBUG)){
            log(new LoggingEvent(name,LoggingLevel.DEBUG,msg,throwable, params));
        }
    }
    
    public final void trace(CharSequence msg, Object ... params) {
        trace(null,msg,params);
    }
    
    public void trace(Throwable throwable,CharSequence msg, Object ... params) {
        if (level.canLog(LoggingLevel.WARN)){
            log(new LoggingEvent(name,LoggingLevel.WARN,msg,throwable, params));
        }
    }
    
    public boolean isEnabled(LoggingLevel testlevel){
        return level.canLog(testlevel);
    }
    
    public final void log(LoggingEvent event){
    	 if (level.canLog(event.getLevel())){
    		 doLog(event);
         }
    }
    
    protected abstract void doLog(LoggingEvent event);
    public abstract LogBook addWriter(LogBookWriter writer);
}
