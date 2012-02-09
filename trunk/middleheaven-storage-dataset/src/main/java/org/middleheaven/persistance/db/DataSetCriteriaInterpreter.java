/**
 * 
 */
package org.middleheaven.persistance.db;

import org.middleheaven.persistance.SearchPlan;

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