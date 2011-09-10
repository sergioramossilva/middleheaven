package org.middleheaven.core.wiring.activation;

import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.wiring.WiringService;

/**
 * Allows for {@link Activator}s to be added and removed from it as an collection. Then when scanned loads the activators present.
 */
public class SetActivatorScanner extends AbstractActivatorScanner {

	private Set<Class<? extends Activator>> units = new HashSet<Class<? extends Activator>>();

	/**
	 * 
	 * Constructor.
	 */
	public SetActivatorScanner(){}
	
	/**
	 * Adds and {@link Activator} class.
	 * 
	 * @param activatorType the class of the activator.
	 * @return this object.
	 */
	public SetActivatorScanner addActivator(Class<? extends Activator> activatorType){
		this.units.add(activatorType);
		return this;
	}
	
	/**
	 * Removed and {@link Activator} class.
	 * 
	 * @param activatorType the class of the activator.
	 * @return  this object.
	 */
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
