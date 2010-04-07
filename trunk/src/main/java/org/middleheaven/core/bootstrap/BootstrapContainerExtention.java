package org.middleheaven.core.bootstrap;

public interface BootstrapContainerExtention {

	/**
	 * 
	 * @param context
	 * @param chain
	 */
	public void extend(BootstrapContext context, BootstrapChain chain);
}
