/**
 * 
 */
package org.middleheaven.persistance;


/**
 * 
 */
public interface DataQuery {

	/**
	 * Returns the query {@link DataRowStream}. Subsequent calls to this method
	 * will return different , reset, {@link DataRowStream}
	 * @return the query {@link DataRowStream}.
	 */
	public DataRowStream getRowStream();
	
	/**
	 * 
	 * @return the quantity of rows that will be found if this query is applied;
	 */
	public long rowCount();
	
	/**
	 * @return <code>true</code> if rowCount would return 0,<code>false</code> otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * Creates a new query base on the same criteria used for <code>this</code>
	 * but with limited elements 
	 * 
	 * @param startAt ordinal position of the first row in the new query (1 is the first)
	 * @param maxCount the maximum quantity of rows to read. 
	 * @return a new, limited, {@link DataQuery}.
	 */
	public DataQuery limit(int startAt, int maxCount);
}
