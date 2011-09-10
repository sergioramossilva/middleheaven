
package org.middleheaven.logging;

public class LoggingException extends RuntimeException {


    protected LoggingException(Exception e){
        super(e);
    }
}
