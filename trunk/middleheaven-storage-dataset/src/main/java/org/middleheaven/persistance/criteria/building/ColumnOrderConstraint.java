/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class ColumnOrderConstraint implements OrderConstraint {

	private boolean isAcendant;
	private QualifiedName qualifiedName;

	/**
	 * Constructor.
	 * @param qualifiedName
	 * @param b
	 */
	public ColumnOrderConstraint(QualifiedName qualifiedName, boolean isAcendant) {
		this.isAcendant = isAcendant;
		this.qualifiedName = qualifiedName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDescendant() {
		return !isAcendant;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAcendant() {
		return isAcendant;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualifiedName getColumnName() {
		return qualifiedName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isAlias() {
		return false;
	}

}
