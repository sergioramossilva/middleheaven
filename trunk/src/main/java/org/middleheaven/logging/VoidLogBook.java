
package org.middleheaven.logging;


/**
 * This LogBook consumes all events without registering then anywhere.
 * It`s a NO-OP LogBook
 *
 * @author Sergio M. M. Taborda
 *
 */
class VoidLogBook extends LogBook{

    private static VoidLogBook me;
    protected static VoidLogBook getInstance(){
        if (me==null){
            me = new VoidLogBook();
        }
        return me;
    }
    
    private VoidLogBook() {
        super("voidbook", LoggingLevel.NONE);
    }

    @Override
    public void fatal(Object msg) {}
    @Override
    public  void fatal(Object msg, Throwable throwable) {}
    @Override
    public  void error(Object msg) {}
    @Override
    public  void error(Object msg, Throwable throwable) {}
    @Override
    public  void warn(Object msg) {}
    @Override
    public  void warn(Object msg, Throwable throwable) {}
    @Override
    public  void info(Object msg) {}
    @Override
    public  void info(Object msg, Throwable throwable) {}
    @Override
    public  void debug(Object msg) {}
    @Override
    public  void debug(Object msg, Throwable throwable) {}
    @Override
    public void trace(Object msg) {}

    @Override
    public void trace(Object msg, Throwable throwable) {}

    @Override
    public void log(LoggingEvent event) {}

    @Override
    public LogBook addWriter(LogBookWriter writer) {
    	return this;
    }

    public boolean isEnabled(LoggingLevel testlevel){
        return false;
    }
    
}
