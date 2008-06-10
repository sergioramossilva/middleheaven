/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.global;

import java.text.SimpleDateFormat;

public class ISO8601Format extends SimpleDateFormat {

    
    public ISO8601Format(){
        super("yyyy-MM-ddTHH:mm:ss.SSS");
    }
}
