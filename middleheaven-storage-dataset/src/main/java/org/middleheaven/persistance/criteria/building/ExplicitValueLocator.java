/**
 * 
 */
package org.middleheaven.persistance.criteria.building;


/**
 * 
 */
public class ExplicitValueLocator  extends ColumnValueLocator {

	private Object value;

	/**
	 * Constructor.
	 * @param value
	 */
	public ExplicitValueLocator(Object value) {
		this.value = value;
	}

	public Object getValue(){
		return value;
	}

	public String toString(){
		return String.valueOf(value);
	}
	
}
