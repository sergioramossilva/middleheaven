package org.middleheaven.tool.test;

import org.middleheaven.core.bootstrap.client.AbstractStandaloneBootstrapEnvironment;

public class TestBootstrapEnvironment extends AbstractStandaloneBootstrapEnvironment {


	protected TestBootstrapEnvironment(){
		super();
	}
	
    @Override
    public String getName() {
        return "Test Container";
    }



}
