package org.middleheaven.core.wiring.activation;

public interface DeployableScannerListener {

	
	public void onDeployableFound(DeployableScanEvent event);
	public void onDeployableLost(DeployableScanEvent event);
}
