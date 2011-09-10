
package org.middleheaven.logging;

/**
 * LoggingLevel is used to determine if a <code>LogEvent</code> should be logged given a determinaed level for the book or writer reciving the event.
 * @author  Sergio M. M. Taborda
 */
public enum LoggingLevel {

    NONE(0),
    FATAL(10),
    ERROR(20),
    WARN(30),
    INFO(40),
    DEBUG(50),
    TRACE(60),
    ALL(70);


    private int level;
    LoggingLevel(int level){
        this.level = level;
    }


    public int getLevel(){
        return level;
    }

    /**
     * Test if this level must be logged for a given configuration level.
     * exmple:
     *  false == INFO.canLog(DEBUG)
     *  true  == INFO.canLog(INFO)
     *  true  == INFO.canLog(ERROR)
     *
     * @param other
     * @return
     */
    public boolean canLog(LoggingLevel configurationLevel){
        return this.level >= configurationLevel.level;
    }

}
