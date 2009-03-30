package org.middleheaven.tool.test;

import java.io.File;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.io.repository.ManagedFileRepositories;

public class TestBootstrap extends ExecutionEnvironmentBootstrap{

	Container container;
	public TestBootstrap(File root){
		container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(root));
	}
	@Override
	public Container getContainer() {
		return container;
	}



}
