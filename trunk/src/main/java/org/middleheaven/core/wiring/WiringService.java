package org.middleheaven.core.wiring;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.core.dependency.DependencyResolver;
import org.middleheaven.core.wiring.DefaultWiringService.StarterMy;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.ActivatorDependencyResolver;
import org.middleheaven.core.wiring.activation.ActivatorScanner;
import org.middleheaven.core.wiring.activation.UnitActivatorDepedencyModel;
import org.middleheaven.logging.Logging;

/**
 * Wire Service 
 * Provides access to the current <code>WiringContext</code>
 */
public interface WiringService {

	
	public ObjectPool getObjectPool();
	public void addConnector(WiringConnector ... connectors); 
	
	public void addActivatorScanner(ActivatorScanner scanner);
	public void removeActivatorScanner(ActivatorScanner scanner);
	public void scan();
}
