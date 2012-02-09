/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class ColumnGroupConstraint  {

	private QualifiedName qualifiedName;

	/**
	 * Constructor.
	 * @param qualifiedName
	 */
	public ColumnGroupConstraint(QualifiedName qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	/**
	 * @return
	 */
	public QualifiedName getColumnName() {
		return qualifiedName;
	}

}
