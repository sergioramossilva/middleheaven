package org.middleheaven.core.bootstrap.activation;

public final class ActivatorScannerEvent {

	private Class<? extends ServiceActivator> activator;

	public ActivatorScannerEvent(Class<? extends ServiceActivator> activator) {
		super();
		this.activator = activator;
	}

	public Class<? extends ServiceActivator> getActivatorType() {
		return activator;
	}
	
	
}
