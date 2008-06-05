/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.dependency;


public interface Installable extends Dependante{


    public void addDependency(Dependency d);
    public void removeDependency(Dependency d);
    
    /**
     * Called after all dependencies were met and installed
     * @param context
     */
    public void install(InstallContext context);
    public boolean isInstalled();
    public void setInstalled(boolean value);
    
    
    public boolean matchDependency(Dependency dependency);
}
