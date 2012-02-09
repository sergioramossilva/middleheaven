/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public interface OrderConstraint {

	
	public boolean isAlias();
	
	public boolean isDescendant();
	
	public boolean isAcendant();
	
	public QualifiedName getColumnName();
}