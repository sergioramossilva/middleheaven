/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.dependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class InstallableSupport implements Installable{


    protected final List<Dependency> dependencies = new ArrayList<Dependency>(); 
    
    private boolean isinstalleDependency;
    
    public InstallableSupport(){}
    
    public void addDependency(Dependency Dependency) {
        this.dependencies.add(Dependency);
    }
    
    public void removeDependency(Dependency Dependency) {
        this.dependencies.remove(Dependency);
    }

    public boolean isInstalled(){
        return isinstalleDependency;
    }
    
    public void setInstalled(boolean value) {
        this.isinstalleDependency = value;
    }

    public Collection<Dependency> getDependencies() {
        return dependencies;
    }
}
