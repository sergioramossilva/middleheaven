package org.middleheaven.core.wiring.activation;

import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.wiring.WiringContext;

public class SetDeployableScanner extends AbstractDeployableScanner {

	private Set<Class<? extends UnitActivator>> units = new HashSet<Class<? extends UnitActivator>>();

	public SetDeployableScanner(){}
	
	public SetDeployableScanner addActivator(Class<? extends UnitActivator> activatorType){
		this.units.add(activatorType);
		return this;
	}
	
	public SetDeployableScanner removeActivator(Class<? extends UnitActivator> activatorType){
		this.units.remove(activatorType);
		return this;
	}

	@Override
	public void scan(WiringContext context) {
		for (Class<? extends UnitActivator> activatorType : units){
			fireDeployableFound(activatorType);
		}
	}

}
