/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.application;


import org.middleheaven.dependency.Dependency;
import org.middleheaven.dependency.InstallContext;
import org.middleheaven.dependency.InstallableSupport;

public class ModuleInstallment extends InstallableSupport {

    final Module module;
    public ModuleInstallment(Module module) {
        dependencies.addAll(module.getDependencies());
        this.module = module;
    }

    public void install(InstallContext context) {
        ApplicationInstallContext ac = (ApplicationInstallContext)context;
        this.module.install(context);
        ac.getApplicationModel().addModule(this.module);
    }

    public boolean matchDependency(Dependency dependency) {
        return this.module.getID().equals(((ModuleDependency)dependency).moduleID);

    }

}
