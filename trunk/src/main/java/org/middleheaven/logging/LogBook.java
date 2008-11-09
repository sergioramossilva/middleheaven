
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

    public void fatal(Object msg) {
        if (level.canLog(LoggingLevel.FATAL)){
            log(new LoggingEvent(LoggingLevel.FATAL,msg));
        }
    }

    public void fatal(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.FATAL)){
            log(new LoggingEvent(LoggingLevel.FATAL,msg,throwable));
        }
    }

    public void error(Object msg) {
        if (level.canLog(LoggingLevel.ERROR)){
            log(new LoggingEvent(LoggingLevel.ERROR,msg));
        }
    }

    public void error(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.ERROR)){
            log(new LoggingEvent(LoggingLevel.ERROR,msg,throwable));
        }
    }


    public void warn(Object msg) {
        if (level.canLog(LoggingLevel.WARN)){
            log(new LoggingEvent(LoggingLevel.WARN,msg));
        }
    }

    public void warn(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.WARN)){
            log(new LoggingEvent(LoggingLevel.WARN,msg,throwable));
        }
    }

    public void info(Object msg) {
        if (level.canLog(LoggingLevel.INFO)){
            log(new LoggingEvent(LoggingLevel.INFO,msg));
        }
    }

    public void info(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.INFO)){
            log(new LoggingEvent(LoggingLevel.INFO,msg,throwable));
        }
    }

    public void debug(Object msg) {
        if (level.canLog(LoggingLevel.DEBUG)){
            log(new LoggingEvent(LoggingLevel.DEBUG,msg));
        }
    }

    public void debug(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.DEBUG)){
            log(new LoggingEvent(LoggingLevel.DEBUG,msg,throwable));
        }
    }

    public void trace(Object msg) {
        if (level.canLog(LoggingLevel.TRACE)){
            log(new LoggingEvent(LoggingLevel.TRACE,msg));
        }
    }

    public void trace(Object msg, Throwable throwable) {
        if (level.canLog(LoggingLevel.TRACE)){
            log(new LoggingEvent(LoggingLevel.TRACE,msg,throwable));
        }
    }

    public boolean isEnabled(LoggingLevel testlevel){
        return level.canLog(testlevel);
    }
    
    public abstract void log(LoggingEvent event);
    public abstract LogBook addWriter(LogBookWriter writer);
}
