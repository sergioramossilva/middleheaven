package org.middleheaven.core.bootstrap;

import org.middleheaven.core.wiring.activation.Activator;

public interface BootstrapContext {

	public BootstrapContext addActivator(Class<? extends Activator> type);
	public BootstrapContext removeActivator(Class<? extends Activator> type);
	public boolean contains(Class<? extends Activator> type);
	

}
