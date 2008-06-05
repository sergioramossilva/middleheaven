/*
 * Created on 2006/11/11
 *
 */
package org.middleheaven.config;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import org.middleheaven.dependency.Dependency;
import org.middleheaven.global.ISO8601Format;
import org.middleheaven.util.StringUtils;

public class HashConfiguration implements Configuration{

    private Collection<Dependency> dependencies = new LinkedList<Dependency>();
    private Map<String, Object > parameters = new HashMap<String, Object >();
    
    public HashConfiguration(){
        
    }
    
    public HashConfiguration(Properties properties){
        this.addParameters(properties);
    }
 
    
    public Properties getAsProperties(){
        Properties p=  new Properties();
        p.putAll(this.parameters);
        return p;
    }
    
    public Collection<Dependency> getDependencies() {
        return Collections.unmodifiableCollection(dependencies);
    }

    protected void addDependencies(Collection<Dependency> dependencies){
        this.dependencies.addAll(dependencies);
    }
    
    public Object getParameter(String name) {
        return parameters.get(name);
    }
    
    public String getStringParameter(String name) {
        return StringUtils.objectToString(parameters.get(name), null);
    }
    
    public Number getNumberParameter(String name) {
        return parameters.get(name)==null ? null : new BigDecimal(parameters.get(name).toString());
    }

    public Date getTimeStampParameter(String name){
        return getTimeStampParameter(name, new ISO8601Format());
    }

    public Date getTimeStampParameter(String name,DateFormat readFormat ) {
        if (parameters.get(name)!=null){
            try {
                return readFormat.parse(parameters.get(name).toString());
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
    
    public void addParameter(String name, Object value){
        this.parameters.put(name, value);
    }
    
    public void addParameters(Properties properties){
        for (Map.Entry entry : properties.entrySet() ){
            this.parameters.put(entry.getKey().toString(), entry.getValue());
        }
    }
}
