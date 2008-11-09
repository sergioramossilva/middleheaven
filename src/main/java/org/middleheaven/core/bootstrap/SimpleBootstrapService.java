/**
 * 
 */
package org.middleheaven.core.bootstrap;

import org.middleheaven.core.Container;

public class SimpleBootstrapService implements BootstrapService{
	
	private Container container;
	
	public SimpleBootstrapService(Container container) {
		this.container = container;
	}

	@Override
	public Container getContainer() {
		return container;
	}
	
}