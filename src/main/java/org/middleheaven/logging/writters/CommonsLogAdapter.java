package org.middleheaven.logging.writters;

import org.middleheaven.logging.Log;
import org.middleheaven.logging.LogBook;


/**
 * Adpater class for Apache Commons Logging.
 * This class implements <code>org.apache.commons.logging.Log</code>.
 * This class only responds to FATAL and ERROR events
 * TODO provide by proxy
 */
public class CommonsLogAdapter implements org.apache.commons.logging.Log {


    final LogBook book;

    public CommonsLogAdapter(String bookName){
        this.book = Log.onBook(bookName);
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

    public void trace(Object message) {}

    public void trace(Object message, Throwable e) {}

    public void debug(Object message) {}

    public void debug(Object message, Throwable e) {}

    public void info(Object message) {}

    public void info(Object message, Throwable e) {}

    public void warn(Object message) {}

    public void warn(Object message, Throwable e) {
       book.trace(e,"{0}", message);
    }

    public void error(Object message) {
    	book.error("{0}", message);
    }

    public void error(Object message, Throwable e) {
    	book.error(e,"{0}", message);
    }

    public void fatal(Object message) {
    	book.fatal("{0}", message);
    }

    public void fatal(Object message, Throwable e) {
    	book.fatal(e,"{0}", message);
    }

}
