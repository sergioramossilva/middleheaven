/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.application;

import org.middleheaven.dependency.DependencySupport;

public class ModuleDependency extends DependencySupport{

    String moduleID;
    public ModuleDependency(String moduleID) {
       this.moduleID = moduleID;
    }
    
    

}
