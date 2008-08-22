/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.middleheaven.io.repository.FileNotFoundManagedException;


public class ManagedIOException extends RuntimeException {

    
	private static final long serialVersionUID = 5373879198361157788L;

	public static ManagedIOException manage(IOException ioe) {
        if (ioe instanceof FileNotFoundException){
            return new FileNotFoundManagedException(ioe.getMessage());
        } else if  (ioe instanceof SocketTimeoutException){
            return new RemoteComunicationTimeoutException(ioe.getMessage());
        }
        return new ManagedIOException(ioe);
    }
    
    public ManagedIOException (Throwable cause){
        super(cause);
    }
    
    public ManagedIOException (String msg){
        super(msg);
    }
    
    public ManagedIOException (String msg,Throwable cause){
        super(msg,cause);
    }
    
}
