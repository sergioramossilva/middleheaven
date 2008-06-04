
package org.middleheaven.logging;

public class LogWritingException extends LoggingException{

	private static final long serialVersionUID = 1L;

	protected LogWritingException(Exception e){
        super(e);
    }
}
