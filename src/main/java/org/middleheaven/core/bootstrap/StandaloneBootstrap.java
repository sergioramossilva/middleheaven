/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap;

import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;


/**
 * @author  Sergio M. M. Taborda 
 */
public class StandaloneBootstrap extends ExecutionEnvironmentBootstrap {

    Container env;
    
    public StandaloneBootstrap(Container env){
      this.env = env;
    }
    
    @Override
    public ContextIdentifier getContextIdentifier() {
        return ContextIdentifier.getInstance("app");
    }
    

    protected void doAfterStop(){
        System.exit(0);
    }

	@Override
	public Container getContainer() {
		return env;
	}



}
