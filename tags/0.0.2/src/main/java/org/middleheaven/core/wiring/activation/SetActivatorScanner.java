package org.middleheaven.core.wiring.activation;

import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.wiring.ObjectPool;
import org.middleheaven.core.wiring.WiringService;

public class SetActivatorScanner extends AbstractActivatorScanner {

	private Set<Class<? extends Activator>> units = new HashSet<Class<? extends Activator>>();

	public SetActivatorScanner(){}
	
	public SetActivatorScanner addActivator(Class<? extends Activator> activatorType){
		this.units.add(activatorType);
		return this;
	}
	
	public SetActivatorScanner removeActivator(Class<? extends Activator> activatorType){
		this.units.remove(activatorType);
		return this;
	}

	@Override
	public void scan(WiringService wiringService) {
		for (Class<? extends Activator> activatorType : units){
			fireDeployableFound(activatorType);
		}
	}

	public boolean contains(Class<? extends Activator> activatorType) {
		return units.contains(activatorType);
	}

	public String toString(){
		return units.toString();
	}
}
