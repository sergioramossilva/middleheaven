/*
 * Created on 2006/12/30
 *
 */
package org.middleheaven.application;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.config.Parameterized;

public abstract class ParameterizedModule extends Module implements Parameterized{


    private final Map <String , String > params = new HashMap<String,String>();
    
    public final String getParameter(String key){
        return params.get(key);
    }
    
    public final void setParameter(String key,String value){
        params.put(key, value);
    }
    
    public final void setAllParameters(Map<String,String> iniparams){
        params.putAll(iniparams);
    }
}
