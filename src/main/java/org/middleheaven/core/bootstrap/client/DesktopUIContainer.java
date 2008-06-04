/*
 * Created on 2006/11/25
 *
 */
package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFile;


public class DesktopUIContainer extends StandaloneContainer {

    
    public DesktopUIContainer(ManagedFile repository) {
		super(repository);
	}

	public void init(ExecutionEnvironmentBootstrap bootstrap){
        startUI();
    }
        
    public void startUI(){
        
    }

	@Override
	public void stop(ExecutionEnvironmentBootstrap bootstrap) {
		// TODO Auto-generated method stub
		
	}


}
