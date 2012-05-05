/**
 * 
 */
package org.middleheaven.core.bootstrap;

/**
 * 
 */
public class InstanceBootstrapEnviromentResolver implements
		BootstrapEnvironmentResolver {

	private BootstrapEnvironment implementations;

	public InstanceBootstrapEnviromentResolver(BootstrapEnvironment implementations){
		this.implementations = implementations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BootstrapEnvironment resolveEnvironment(BootstrapContext context) {
		return implementations;
	}

}
