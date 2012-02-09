/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import java.util.Collection;

/**
 * TODO change name to ValuesBagValueLocator
 */
public class MultipleValueLocator extends ColumnValueLocator {

	

	private Collection<ColumnValueLocator> values;

	public MultipleValueLocator (Collection<ColumnValueLocator> values){
		this.values = values;
	}

	public Collection<ColumnValueLocator> getValues() {
		return values;
	}

	
	
	
}
