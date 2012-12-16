/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import org.middleheaven.persistance.model.ColumnValueType;

/**
 * 
 */
public interface EditableDatasetColumnModel extends DatasetColumnModel{

	/**
	 * @return logic name.
	 */
	public void setName(String name);

	/**
	 * @return logic name.
	 */
	public void setHardName(String String);
	
	public void setValueType(ColumnValueType type);
	
	public void setSize (int length);
	
	public void setScale (int scale);
	
	public void setPrecision (int precision);
	
	public void setUpdatable(boolean updatable);

	public void setInsertable(boolean insertable);

	public void setNullable (boolean nullable);

	public void setUnique(boolean unique);
	
	public void setUniqueGroupName(String uniqueGroupName);
	
	public void setVersion(boolean version);
	
	public void setKey(boolean version);
	
	public void setIndexed(boolean version);


}
