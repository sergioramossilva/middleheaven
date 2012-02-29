package org.middleheaven.tool.test;

import org.middleheaven.core.bootstrap.client.AbstractStandaloneContainer;

public class TestContainer extends AbstractStandaloneContainer {


	protected TestContainer(){
		super();
	}
	
    @Override
    public String getContainerName() {
        return "Test Container";
    }

}
