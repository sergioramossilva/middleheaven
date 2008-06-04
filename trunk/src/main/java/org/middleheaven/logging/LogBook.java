
package org.middleheaven.logging;




/**
 * Represents a repository where logEvents are registered.
 * @author  Sergio M. M. Taborda
 */
public abstract class LogBook {

    protected final String name;
    protected final LoggingLevel level;

    protected LogBook(String name,LoggingLevel level){
        this.name = name;
        this.level = level;
    }

    public void logFatal(Object msg) {
        if (level.canLog(LoggingLevel.FATAL)){
            log(new LoggingEvent(LoggingLevel.FATAL,msg));
        }
    }

    public void logFatal(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.FATAL)){
            log(new LoggingEvent(LoggingLevel.FATAL,msg,throwable));
        }
    }

    public void logError(Object msg) {
        if (level.canLog(LoggingLevel.ERROR)){
            log(new LoggingEvent(LoggingLevel.ERROR,msg));
        }
    }

    public void logError(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.ERROR)){
            log(new LoggingEvent(LoggingLevel.ERROR,msg,throwable));
        }
    }


    public void logWarn(Object msg) {
        if (level.canLog(LoggingLevel.WARN)){
            log(new LoggingEvent(LoggingLevel.WARN,msg));
        }
    }

    public void logWarn(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.WARN)){
            log(new LoggingEvent(LoggingLevel.WARN,msg,throwable));
        }
    }

    public void logInfo(Object msg) {
        if (level.canLog(LoggingLevel.INFO)){
            log(new LoggingEvent(LoggingLevel.INFO,msg));
        }
    }

    public void logInfo(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.INFO)){
            log(new LoggingEvent(LoggingLevel.INFO,msg,throwable));
        }
    }

    public void logDebug(Object msg) {
        if (level.canLog(LoggingLevel.DEBUG)){
            log(new LoggingEvent(LoggingLevel.DEBUG,msg));
        }
    }

    public void logDebug(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.DEBUG)){
            log(new LoggingEvent(LoggingLevel.DEBUG,msg,throwable));
        }
    }

    public void logTrace(Object msg) {
        if (level.canLog(LoggingLevel.TRACE)){
            log(new LoggingEvent(LoggingLevel.TRACE,msg));
        }
    }


    public void logTrace(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.TRACE)){
            log(new LoggingEvent(LoggingLevel.TRACE,msg,throwable));
        }

    }


    public boolean isEnabled(LoggingLevel testlevel){
        return level.canLog(testlevel);
    }
    
    public abstract void log(LoggingEvent event);
    public abstract void addWriter(LogBookWriter writer);
}
