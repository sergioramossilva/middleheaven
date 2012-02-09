/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.DataSetConstraint;
import org.middleheaven.util.criteria.CriterionOperator;

/**
 * 
 */
public class ColumnValueConstraint implements DataSetConstraint{

	private ColumnValueLocator rightValueLocator;
	private ColumnValueLocator leftValuelocator;
	private CriterionOperator operator;

	/**
	 * Constructor.
	 * @param qualifiedName
	 * @param operator
	 */
	public ColumnValueConstraint(ColumnValueLocator leftValuelocator,
			CriterionOperator operator, ColumnValueLocator rightValueLocator) {
		this.leftValuelocator = leftValuelocator;
		this.operator = operator;
		this.rightValueLocator = rightValueLocator;
	}

	public ColumnValueLocator getRightValueLocator() {
		return rightValueLocator;
	}

	public ColumnValueLocator getLeftValuelocator() {
		return leftValuelocator;
	}

	public CriterionOperator getOperator() {
		return operator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	
}
