/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class AliasOrderConstraint implements OrderConstraint {

	private String alias;
	private boolean ascendant;

	/**
	 * Constructor.
	 * @param aliasColumn
	 * @param b
	 */
	public AliasOrderConstraint(String aliasColumn, boolean ascendant) {
		this.alias= aliasColumn;
		this.ascendant = ascendant;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDescendant() {
		return !ascendant;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAcendant() {
		return ascendant;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualifiedName getColumnName() {
		return QualifiedName.qualify(null, this.alias);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isAlias() {
		return true;
	}

}
