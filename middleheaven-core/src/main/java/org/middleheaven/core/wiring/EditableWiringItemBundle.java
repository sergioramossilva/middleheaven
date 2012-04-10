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
public class EditableWiringItemBundle implements WiringItemBundle{

	private Collection<WiringItem> items = new HashSet<WiringItem>();

	public EditableWiringItemBundle(){
		
	}
	
	public EditableWiringItemBundle add (WiringItem item){
		
		items.add(item);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<WiringItem> iterator() {
		return items.iterator();
	}

}
