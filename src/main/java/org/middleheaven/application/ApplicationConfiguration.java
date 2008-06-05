/*
 * Created on 2006/12/30
 *
 */
package org.middleheaven.application;

import java.util.List;

import org.middleheaven.config.HashConfiguration;
import org.middleheaven.dependency.Installable;

public class ApplicationConfiguration extends HashConfiguration {

    private final List<Installable> masterSet;
    public ApplicationConfiguration(List<Installable> masterSet){
        this.masterSet = masterSet;
        for (Installable i : masterSet){
           this.addDependencies(i.getDependencies());
        }
    }
    
    public List<Installable> getInstallables(){
        return masterSet;
    }
    
}
