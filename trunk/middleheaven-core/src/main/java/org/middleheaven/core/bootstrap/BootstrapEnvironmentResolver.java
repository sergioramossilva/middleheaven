/**
 * 
 */
package org.middleheaven.core.bootstrap;

/**
 * Determines the {@link BootstrapEnvironment} implementation to use.
 */
public interface BootstrapEnvironmentResolver {

	
	/**
	 * 
	 * @param context
	 * @return
	 */
	BootstrapEnvironment resolveEnvironment(BootstrapContext context);
}
