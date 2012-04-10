package org.middleheaven.tool.test;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;

public class TestBootstrap extends ExecutionEnvironmentBootstrap {

	BootstrapContainer container;

	public TestBootstrap(){
		container = new TestContainer();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public BootstrapContainer resolveContainer() {
		return container;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getExecutionConfiguration() {
		return "test";
	}



}
