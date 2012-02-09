/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.util.QualifiedName;


/**
 * 
 */
public abstract class ColumnValueLocator {

	/**
	 * Return the column qualified name or null if this locator is not by name.
	 * @return Return the column qualified name or null if this locator is not by name.
	 */
	public  QualifiedName getName() {
		return null;
	}
	
	
}
