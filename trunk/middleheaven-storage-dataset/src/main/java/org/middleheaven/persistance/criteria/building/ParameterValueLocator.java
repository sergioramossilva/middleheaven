/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

/**
 * 
 */
public class ParameterValueLocator  extends ColumnValueLocator {

	private final String parameterName;

	/**
	 * Constructor.
	 * @param value
	 */
	public ParameterValueLocator(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterName() {
		return parameterName;
	}

	public String toString(){
		return ":" + parameterName;
	}
	
}
