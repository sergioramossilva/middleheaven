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

	public static ManagedIOException manage(IOException e) {
        if (e instanceof FileNotFoundException){
            return new FileNotFoundManagedException(e.getMessage());
        } else if  (e instanceof SocketTimeoutException){
            return new RemoteComunicationTimeoutException(e.getMessage());
        } 
        return new ManagedIOException(e);
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
