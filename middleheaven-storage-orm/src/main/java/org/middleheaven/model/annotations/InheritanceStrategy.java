/**
 * 
 */
package org.middleheaven.model.annotations;

/**
 * 
 */
public enum InheritanceStrategy {

	//JOINED,
	//TABLE_PER_CLASS,
	SINGLE_DATASET,
	NO_INHERITANCE;

	/**
	 * @return
	 */
	public boolean isSingleDataset() {
		return this == SINGLE_DATASET;
	}

	/**
	 * @return
	 */
	public boolean hasInheritance() {
		return this == SINGLE_DATASET;
	}
}
