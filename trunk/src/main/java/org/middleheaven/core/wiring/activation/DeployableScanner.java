package org.middleheaven.core.wiring.activation;

public interface DeployableScanner {

	public void scan();
	
	public void addScannerListener(DeployableScannerListener listener);
	public void removeScannerListener(DeployableScannerListener listener);
}
