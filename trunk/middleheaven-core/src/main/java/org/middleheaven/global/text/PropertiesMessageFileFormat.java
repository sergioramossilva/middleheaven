/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global.text;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

import org.middleheaven.io.ManagedIOException;

public class PropertiesMessageFileFormat extends LocalizedMessagesFormatHandler{

    PropertyResourceBundle bunble;
    public PropertiesMessageFileFormat(){
        
    }
    
    protected PropertiesMessageFileFormat(PropertyResourceBundle bunble){
        this.bunble = bunble;
    }
    
    public LocalizedMessagesFormatHandler newFormatHandler(InputStream stream){
        try {
            return new PropertiesMessageFileFormat(new PropertyResourceBundle(stream));
        } catch (IOException e) {
            throw ManagedIOException.manage(e);
        }
    }
    
    @Override
    public String findLabel(TextLocalizable label) throws MissingResourceException {
        
        String message = bunble.getString(label.getMessageKey());
        Object [] messageParams = label.getMessageParams();
        if (messageParams.length >0 || message.indexOf("{") >=0){
            final MessageFormat mf = new MessageFormat(message);
            return mf.format(messageParams);
        } else {
            return message;
        }
    }

}
