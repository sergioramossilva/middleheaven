package org.middleheaven.core.wiring.activation;

import org.middleheaven.core.wiring.WiringContext;

public interface DeployableScanner {

	public void scan(WiringContext context);
	
	public void addScannerListener(DeployableScannerListener listener);
	public void removeScannerListener(DeployableScannerListener listener);
}
