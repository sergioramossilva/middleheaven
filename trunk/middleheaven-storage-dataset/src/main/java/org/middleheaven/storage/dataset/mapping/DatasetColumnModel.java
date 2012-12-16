/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import org.middleheaven.persistance.model.ColumnValueType;

/**
 * 
 */
public interface DatasetColumnModel {

	/**
	 * @return logic name.
	 */
	public String getName();

	/**
	 * @return logic name.
	 */
	public String getHardName();
	
	public ColumnValueType getValueType();
	
	public int getSize ();
	
	public int getScale ();
	
	public int getPrecision ();
	
	public boolean isUpdatable();

	public boolean isInsertable();

	public boolean isNullable ();

	public boolean isUnique();
	
	public String getUniqueGroupName();

	/**
	 * @return
	 */
	public boolean isKey();
	
	public boolean isIndexed();
}
