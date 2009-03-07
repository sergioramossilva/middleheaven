package org.middleheaven.storage.assembly;

public interface Assembler {

	/**
	 * 
	 * @return Class that his assemble can handle
	 */
	public Class<?> getAssembleType();
	
	/**
	 * Converts raw data into an object
	 * @param context from where to read the raw data
	 * @param line where to place the object
	 */
	public void assemble(AssemblyLineService service, AssemblyContext context, Data data);
	
	/**
	 * Converts an object into raw data
	 * @param context to where to write the raw data
	 * @param line from where to read the object
	 */
	public void unAssemble(AssemblyLineService service, AssemblyContext context, Data data);
}
