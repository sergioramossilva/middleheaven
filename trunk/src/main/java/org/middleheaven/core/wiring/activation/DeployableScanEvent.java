package org.middleheaven.core.wiring.activation;

public final class DeployableScanEvent {

	private Class<? extends UnitActivator> activator;

	public DeployableScanEvent(Class<? extends UnitActivator> activator) {
		super();
		this.activator = activator;
	}

	public Class<? extends UnitActivator> getActivatorType() {
		return activator;
	}
	
	
}
