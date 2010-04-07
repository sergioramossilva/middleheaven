package org.middleheaven.tool.test;

import org.middleheaven.core.bootstrap.client.AbstractStandaloneContainer;
import org.middleheaven.io.repository.ManagedFile;

public class TestContainer extends AbstractStandaloneContainer {


	protected TestContainer( ManagedFile root){
		super(root);
	}
	
    @Override
    public String getContainerName() {
        return "Test Container";
    }

}
