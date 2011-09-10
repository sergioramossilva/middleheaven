package org.middleheaven.core.wiring.activation;

public interface ActivatorScannerListener {

	
	public void onActivatorFound(ActivatorScannerEvent event);
	public void onActivatorLost(ActivatorScannerEvent event);
}
