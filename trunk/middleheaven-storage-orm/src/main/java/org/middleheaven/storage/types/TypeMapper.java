package org.middleheaven.storage.types;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;

public interface TypeMapper {
	    
	/**
	 * 
	 * @return Class that his assemble can handle
	 */
	public String getMappedClassName();
	
	/**
	 * Map an object from a row.
	 * 
	 * 
	 * @param row the row to map to the object
	 * @param aggregateParent the aggregate parent, or <code>null</code> if no parent is available. aggregateParent is used for embedbed objects. 
	 * @param columns the columns to use from the row
	 * @return
	 */
	public Object read(DataRow row, Object aggregateParent,  DataColumnModel ... columns );
	
	/**
	 * Map an object to some columns of the row
	 * @param parent the object that agrregates this object, if any
	 * @param object the object to map
	 * @param row the row to fill with data from the object
	 * @param columns the columns to fill in the row
	 */
	public void write(Object parent , Object object,  DataRow row, DataColumnModel ... columns );
	
	
}
