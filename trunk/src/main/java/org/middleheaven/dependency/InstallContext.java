/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.dependency;

import java.util.Collection;

import org.middleheaven.core.Container;



/**
 * Generic Install Context. The install context is responsible for 
 * installing all <code>Installable</code>s in it. Also it may be used 
 * pass install-time information to the <code>Installable</code>s
 * 
 * @author Sergio M. M. Taborda 
 *
 */
public class InstallContext {


    protected final Collection<Installable> installments;
    //protected final InstallableDependencyMatcher<Installable, Dependency> matcher;
    private final Container environment;
    
    public  InstallContext(Container environment, Collection<Installable> installments ){
        this.installments = installments;
       // this.matcher = matcher;
        this.environment = environment;
    }

    public Container getExecutionEnvironment(){
        return environment;
    }

    /**
     * Installs all <code>Installable</code>s and they dependencies.
     *
     */
    public void installAll(){
        for (Installable ins: this.installments){
            install(ins);
        }
    }


    private final Installable getInstallable(Dependency dependency) throws InstallableNotFoundException{

        // compare parameters to aquire the correct installable

        for (Installable ins : installments){
            if (ins.matchDependency(dependency)){
                // match , return this installable
                return ins;
            }
        }
        throw new InstallableNotFoundException(dependency.toString());


    }

    private final void install(Installable installable) throws InstallableNotFoundException{

        if (installable.isInstalled()) return;


        // install dependencies first
        for (Dependency dependency : installable.getDependencies()){
            
                try {
                    this.getInstallable(dependency).install(this);
                } catch (InstallableNotFoundException e){
                    // the dependency was not found. 
                    // this will only trigger an exception  
                    // if the dependency is required
                    if (dependency.isRequired()){
                        throw e;
                    }
                }
            
        }

        // install Installable if all dependencies were met
        installable.install(this);

        installable.setInstalled(true);
    }
}
