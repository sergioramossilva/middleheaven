/*
 * Created on 2006/11/11
 *
 */
package org.middleheaven.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public abstract class InputStreamConfigurator implements Configurator{


    
    protected InputStream getInputStream(ConfigurationContext context) throws IOException{
        Object source = context.getConfigurationData();
        
        if (source instanceof File){
            return new FileInputStream((File)source);
        } else if (source instanceof URL){
            return ((URL)source).openStream();
        } 
        
        throw new IOException("Cannot convert Configuration Data to an InputStream ");
    }

}
