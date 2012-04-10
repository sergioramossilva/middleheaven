/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 
 */
public class InstancesWiringItemBundle implements WiringItemBundle {

	
	private Collection<WiringItem> instances = new HashSet<WiringItem>();

	public InstancesWiringItemBundle (){}
	
	public InstancesWiringItemBundle(Object instance){
		this.instances.add(new InstanceWiringItem(instance));
	}
	
	public InstancesWiringItemBundle add(Object instance){
		this.instances.add(new InstanceWiringItem(instance));
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<WiringItem> iterator() {
		return instances.iterator();
	}

}
