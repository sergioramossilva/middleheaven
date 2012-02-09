/**
 * 
 */
package org.middleheaven.persistance;

/**
 * A parameterized {@link DataQuery}.
 * To execute this query the parameters must be provided.
 */
public interface ParameterizedDataQuery extends DataQuery{

	
	public void setParameter(String parameterName, Object parameterValue);
}
