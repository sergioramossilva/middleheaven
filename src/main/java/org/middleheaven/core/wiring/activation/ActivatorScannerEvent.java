package org.middleheaven.core.wiring.activation;

public final class ActivatorScannerEvent {

	private Class<? extends Activator> activator;

	public ActivatorScannerEvent(Class<? extends Activator> activator) {
		super();
		this.activator = activator;
	}

	public Class<? extends Activator> getActivatorType() {
		return activator;
	}
	
	
}
