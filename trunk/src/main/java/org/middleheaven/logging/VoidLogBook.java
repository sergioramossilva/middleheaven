
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
    public void logFatal(Object msg) {}
    @Override
    public  void logFatal(Object msg, Throwable throwable) {}
    @Override
    public  void logError(Object msg) {}
    @Override
    public  void logError(Object msg, Throwable throwable) {}
    @Override
    public  void logWarn(Object msg) {}
    @Override
    public  void logWarn(Object msg, Throwable throwable) {}
    @Override
    public  void logInfo(Object msg) {}
    @Override
    public  void logInfo(Object msg, Throwable throwable) {}
    @Override
    public  void logDebug(Object msg) {}
    @Override
    public  void logDebug(Object msg, Throwable throwable) {}
    @Override
    public void logTrace(Object msg) {}

    @Override
    public void logTrace(Object msg, Throwable throwable) {}

    @Override
    public void log(LoggingEvent event) {}

    @Override
    public void addWriter(LogBookWriter writer) {}

    public boolean isEnabled(LoggingLevel testlevel){
        return false;
    }
    
}
