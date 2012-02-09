package org.middleheaven.persistance;


/**
 * Provides an interfaces for reading rows from a data set.
 */
public interface DataRowStream {

	/**
	 * Moves to the next row. The DataRowStream, when created is positioned before the first row. 
	 * @return <code>true</code> if there is a current row to read, <code>false</code> otherwise.
	 */
	public boolean next() throws DataRowStreamException;
	
	/**
	 * 
	 * @return the current row.
	 */
	public DataRow currentRow() throws DataRowStreamException;
	
	/**
	 *  close the data stream and release resources.
	 */
	public void close()  throws DataRowStreamException;
}
