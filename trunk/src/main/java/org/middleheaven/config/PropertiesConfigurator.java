/*
 * Created on 2006/11/11
 *
 */
package org.middleheaven.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.middleheaven.io.ManagedIOException;

public class PropertiesConfigurator extends InputStreamConfigurator{

    @SuppressWarnings("unchecked")
    public Configuration getConfiguration(ConfigurationContext context) throws ManagedIOException {
        Properties properties = new Properties();
        
        if (context.getConfigurationData() instanceof Map){
            properties.putAll((Map)context.getConfigurationData());
        } else {
            
            try {
                properties.load(this.getInputStream(context));
            } catch (IOException ioe) {
                throw  ManagedIOException.manage(ioe);  
            } 
        }
        
        return new HashConfiguration(properties);
    }

}
