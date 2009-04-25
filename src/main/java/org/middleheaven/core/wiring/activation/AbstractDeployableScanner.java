package org.middleheaven.core.wiring.activation;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractDeployableScanner implements DeployableScanner {

	private Set<DeployableScannerListener> listeners = new CopyOnWriteArraySet<DeployableScannerListener>();
	
	@Override
	public void addScannerListener(DeployableScannerListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeScannerListener(DeployableScannerListener listener) {
		listeners.remove(listener);
	}

	

}
