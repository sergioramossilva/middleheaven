/**
 * 
 */
package org.middleheaven.persistance.db;

import org.middleheaven.persistance.db.metamodel.DBColumnModel;

/**
 * 
 */
public interface ValueHolder {

	public Object getValue(QueryParameters parameters);

	/**
	 * @return
	 */
	public DBColumnModel getModel();
}
