/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global.text;

import java.io.InputStream;
import java.util.MissingResourceException;


public abstract class LocalizedMessagesFormatHandler {

    public abstract LocalizedMessagesFormatHandler newFormatHandler(InputStream stream);
    
    public abstract String findLabel(GlobalLabel label) throws MissingResourceException;
        
}
