/*
 * Created on 2006/12/27
 *
 */
package org.middleheaven.application;

import java.util.Collection;

import org.middleheaven.dependency.InstallContext;
import org.middleheaven.dependency.Installable;
import org.middleheaven.service.bootstrap.X.ExecutionEnvironment;

public class ApplicationInstallContext extends InstallContext{

    public ApplicationModel model;
    public ApplicationInstallContext(Container environment,ApplicationModel model , Collection<Installable> installments) {
        super(environment,installments);
    }
    
    public ApplicationModel getApplicationModel(){
        return model;
    }

}
