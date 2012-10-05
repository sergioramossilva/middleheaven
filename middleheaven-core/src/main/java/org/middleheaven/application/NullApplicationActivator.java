/**
 * 
 */
package org.middleheaven.application;

/**
 * 
 */
public class NullApplicationActivator implements ApplicationActivator {

	
	public NullApplicationActivator (){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(ApplicationContext context) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeModuleStart(ApplicationContext context) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterModuleStart(ApplicationContext context) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(ApplicationContext context) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeModuleStop(ApplicationContext context) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterModuleStop(ApplicationContext context) {
		//no-op
	}

}
