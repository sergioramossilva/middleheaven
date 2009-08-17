package org.middleheaven.logging.writters;

import org.apache.commons.logging.Log;
import org.middleheaven.logging.Logging;

/**
 * Adpater class for Apache Commons Logging.
 * This class implements <code>org.apache.commons.logging.Log</code>.
 * This class only responds to FATAL and ERROR events
 * TODO provide by proxy
 */
public class CommonsLogAdapter implements Log {


    final String bookName;

    public CommonsLogAdapter(String bookName){
        this.bookName = bookName;
    }

    public boolean isDebugEnabled() {
        return false;
    }

    public boolean isErrorEnabled() {
        return true;
    }

    public boolean isFatalEnabled() {
        return true;
    }

    public boolean isInfoEnabled() {
        return false;
    }

    public boolean isTraceEnabled() {
        return false;
    }

    public boolean isWarnEnabled() {
        return false;
    }

    public void trace(Object arg0) {}

    public void trace(Object arg0, Throwable arg1) {}

    public void debug(Object arg0) {}

    public void debug(Object arg0, Throwable arg1) {}

    public void info(Object arg0) {}

    public void info(Object arg0, Throwable arg1) {}

    public void warn(Object arg0) {}

    public void warn(Object arg0, Throwable arg1) {
        Logging.getBook(this.bookName).trace(arg0,arg1);
    }

    public void error(Object arg0) {
    	Logging.getBook(this.bookName).error(arg0);
    }

    public void error(Object arg0, Throwable arg1) {
    	Logging.getBook(this.bookName).error(arg0,arg1);
    }

    public void fatal(Object arg0) {
    	Logging.getBook(this.bookName).fatal(arg0);
    }

    public void fatal(Object arg0, Throwable arg1) {
    	Logging.getBook(this.bookName).fatal(arg0,arg1);
    }

}
