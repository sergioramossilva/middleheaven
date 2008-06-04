package org.middleheaven.logging.writters;

import org.apache.commons.logging.Log;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.Logging;

/**
 * Classe adpatadora para ser usada com o Commons Logging da Apache.
 * Esta classe só recebe eventos FATAL e ERROR
 * @author <a href="mailto:staborda@gnk.com">Sergio M.M. Taborda</a>
 *
 */
public class CommonsLogAdapter implements Log {


    final String name;
    /**
     *
     * @param loggerName parametro obrigatorio
     *
     */
    public CommonsLogAdapter(String loggerName){
        this.name = loggerName;
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
        Logging.getBook(this.name).logTrace(arg0,arg1);
    }

    public void error(Object arg0) {
    	Logging.getBook(this.name).logError(arg0);
    }

    public void error(Object arg0, Throwable arg1) {
    	Logging.getBook(this.name).logError(arg0,arg1);
    }

    public void fatal(Object arg0) {
    	Logging.getBook(this.name).logFatal(arg0);
    }

    public void fatal(Object arg0, Throwable arg1) {
    	Logging.getBook(this.name).logFatal(arg0,arg1);
    }

}
