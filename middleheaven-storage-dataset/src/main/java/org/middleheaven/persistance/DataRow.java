package org.middleheaven.persistance;

import org.middleheaven.util.QualifiedName;

/**
 * A set of column values in a DataSet.
 * 
 * The iteration of data row must be previsible, meaning each iteration must iterate in the same order
 */
public interface DataRow extends Iterable<DataColumn>{

	/**
	 * 
	 * @param <T> the field type.
	 * @param name the name of the field to get. 
	 * @param type the field's value type.
	 * @return the value of the field.
	 */
	public DataColumn getColumn(QualifiedName name);
	

}
