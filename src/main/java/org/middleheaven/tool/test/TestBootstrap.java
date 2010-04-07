package org.middleheaven.tool.test;

import java.io.File;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFiles;

public class TestBootstrap extends ExecutionEnvironmentBootstrap{

	BootstrapContainer container;
	public TestBootstrap(File root){
		container = new TestContainer(ManagedFiles.resolveFile(root));
	}
	
	
	@Override
	public BootstrapContainer getContainer() {
		return container;
	}



}
