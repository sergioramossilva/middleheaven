
package org.middleheaven.logging;

/**
 * Represents an event to be logged. It records the timestamp of its creation and the thread name that created it.
 * @author  Sergio M. M. Taborda
 */
public class LoggingEvent {
	
	private final long timeStamp;
	private final String threadName;
  
	private final LoggingLevel level;
	private final CharSequence msg;
	private final Throwable t;
    
	private final Object[] params;

    public LoggingEvent(LoggingLevel level, CharSequence msg){
        this(level,msg,null);
    }
    
    public LoggingEvent(LoggingLevel level, CharSequence msg, Throwable t,Object ... params){
        this.timeStamp = System.currentTimeMillis();
        this.level = level;
        this.msg = msg;
        this.t = t;
        this.params = params;
        this.threadName = Thread.currentThread().getName();
    }

	public LoggingLevel getLevel(){
        return level;
    }

    public CharSequence getMessage(){
        return msg;
    }
    
    public Object[] getMessageParameters(){
    	return this.params == null ? new Object[0] : params;
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