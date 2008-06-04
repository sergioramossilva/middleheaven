
package org.middleheaven.logging;

/**
 * Represents an event to be logged. It records the timestamp of its creation and the thread name that created it.
 * @author  Sergio M. M. Taborda
 */
public class LoggingEvent {

  
    protected LoggingLevel level;
  
    protected Object msg;

    protected Throwable t;

    protected final long timeStamp;
 
    protected final String threadName;

    public LoggingEvent(LoggingLevel level, Object msg){
        this(level,msg,null);
    }

    public LoggingEvent(LoggingLevel level, Object msg, Throwable t){
        this.timeStamp = System.currentTimeMillis();
        this.level = level;
        this.msg = msg;
        this.t = t;
        this.threadName = Thread.currentThread().getName();
    }


    public LoggingLevel getLevel(){
        return level;
    }

    public Object getMessage(){
        return msg;
    }

    public boolean hasThrowable(){
        return t!=null;
    }
    public Throwable getThrowable(){
        return t;
    }

    public long getTime(){
        return this.timeStamp;
    }

    public String getThreadName(){
        return this.threadName;
    }
}
