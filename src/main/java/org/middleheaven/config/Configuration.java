/*
 * Created on 2006/11/11
 *
 */
package org.middleheaven.config;

import java.util.Collection;
import java.util.Date;

import org.middleheaven.dependency.Dependency;

public interface Configuration {

    
    public Collection<Dependency> getDependencies();
    
    public Object getParameter(String name);
    
    public String getStringParameter(String name);
 
    public Number getNumberParameter(String name);
    
    public Date getTimeStampParameter(String name);
}
