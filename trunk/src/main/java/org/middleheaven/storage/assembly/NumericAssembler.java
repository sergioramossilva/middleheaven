package org.middleheaven.storage.assembly;

public class NumericAssembler implements Assembler{

	@Override
	public void assemble(DataContext context, AssemblyLine line) {
		// TODO implement Assembler.assemble
		
	}

	@Override
	public Class<?> getAssembleType() {
		return org.middleheaven.util.measure.Number.class;
	}

	@Override
	public void unAssemble(DataContext context, AssemblyLine line) {
		org.middleheaven.util.measure.Number number = (org.middleheaven.util.measure.Number)line.getObject();
		
		context.put(line.getName(), number.asNumber());
	}

}
