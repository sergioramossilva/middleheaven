package org.middleheaven.core.wiring.activation;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractActivatorScanner implements ActivatorScanner {

	private Set<ActivatorScannerListener> listeners = new CopyOnWriteArraySet<ActivatorScannerListener>();
	
	@Override
	public void addScannerListener(ActivatorScannerListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeScannerListener(ActivatorScannerListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireDeployableFound(Class<? extends Activator> type){
		ActivatorScannerEvent event = new ActivatorScannerEvent(type);
		for (ActivatorScannerListener listener : listeners){
			listener.onActivatorFound(event);
		}
	}

	protected void fireDeployableLost(Class<? extends Activator> type){
		ActivatorScannerEvent event = new ActivatorScannerEvent(type);
		for (ActivatorScannerListener listener : listeners){
			listener.onActivatorLost(event);
		}
	}

}
