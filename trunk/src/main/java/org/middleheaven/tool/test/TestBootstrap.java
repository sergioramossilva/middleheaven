package org.middleheaven.tool.test;

import java.io.File;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFileRepositories;

public class TestBootstrap extends ExecutionEnvironmentBootstrap{

	Container container;
	public TestBootstrap(File root){
		container = new TestContainer(ManagedFileRepositories.resolveFile(root));
	}
	@Override
	public Container getContainer() {
		return container;
	}



}
