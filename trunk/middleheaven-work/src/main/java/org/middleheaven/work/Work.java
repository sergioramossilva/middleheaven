package org.middleheaven.work;

/**
 * Unit of work to be executed.
 */
public interface Work {

	/**
	 * Execute the unit of work. 
	 * @param context the context of execution.
	 */
	public void execute(WorkContext context);
}
