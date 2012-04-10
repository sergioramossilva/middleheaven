package org.middleheaven.core.bootstrap.activation;

public interface ActivatorScannerListener {

	
	public void onActivatorFound(ActivatorScannerEvent event);
	public void onActivatorLost(ActivatorScannerEvent event);
}
