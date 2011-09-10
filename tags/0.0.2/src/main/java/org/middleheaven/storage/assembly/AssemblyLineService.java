package org.middleheaven.storage.assembly;

public interface AssemblyLineService {

	public void addAssembler(Assembler assembler);

	public void removeAssembler(Assembler assembler);
	
	public Assembler getAssembler (Class<?> type);
	
	public void unAssemble(AssemblyContext ctx);

	public Object assemble(AssemblyContext ctx);
	
}
