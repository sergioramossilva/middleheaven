package org.middleheaven.tool.test;

import org.middleheaven.core.bootstrap.AbstractBootstrap;
import org.middleheaven.core.bootstrap.BootstrapEnvironmentResolver;
import org.middleheaven.core.bootstrap.InstanceBootstrapEnviromentResolver;

public class TestBootstrap extends AbstractBootstrap {

	public TestBootstrap(){
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getExecutionConfiguration() {
		return "test";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BootstrapEnvironmentResolver bootstrapEnvironmentResolver() {
		return new InstanceBootstrapEnviromentResolver(new TestBootstrapEnvironment());
	}



}
