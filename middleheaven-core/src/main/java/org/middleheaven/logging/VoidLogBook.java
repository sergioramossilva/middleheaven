
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
    public void doLog(LoggingEvent event) {}


    public boolean isEnabled(LoggingLevel testlevel){
        return false;
    }
    
}
