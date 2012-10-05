/**
 * 
 */
package org.middleheaven.persistance.db;


/**
 * 
 */
public interface DataSetCriteriaInterpreter {

	/**
	 * Provides a retrieve command given a search plan.
	 * @param plan
	 * @return the resulting RetriveDataBaseCommand.
	 */
	public RetriveDataBaseCommand translateRetrive(SearchPlan plan);

	public DataBaseCommand translateDelete(SearchPlan plan);

}