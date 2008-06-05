/*
 * Created on 2006/12/27
 *
 */
package org.middleheaven.application;


import org.middleheaven.dependency.Dependency;
import org.middleheaven.dependency.InstallableDependencyMatcher;

public class ModuleDependencyMatcher<I extends ModuleInstallment,D extends ModuleDependency> implements InstallableDependencyMatcher<I,D>{

    public boolean match(I installable, D dependency) {

        return installable.module.getID().equals(((ModuleDependency)dependency).moduleID);

    }

    public boolean isMatchable(Dependency dependency) {
        return dependency instanceof ModuleDependency;
    }

}
