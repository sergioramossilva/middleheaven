package org.middleheaven.core.wiring.activation;

import org.middleheaven.core.wiring.WiringService;

public interface ActivatorScanner {


	public void addScannerListener(ActivatorScannerListener listener);
	public void removeScannerListener(ActivatorScannerListener listener);

	public void scan(WiringService wiringService);
}
