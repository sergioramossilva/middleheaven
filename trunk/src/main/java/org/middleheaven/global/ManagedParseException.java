/*
 * Created on 2006/09/28
 *
 */
package org.middleheaven.global;

import java.text.ParseException;


public class ManagedParseException extends RuntimeException {

    
    public ManagedParseException(ParseException e){
        super(e);
    }
}
