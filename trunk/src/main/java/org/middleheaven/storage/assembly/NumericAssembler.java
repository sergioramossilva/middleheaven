package org.middleheaven.storage.assembly;

public class NumericAssembler implements Assembler{


	@Override
	public Class<?> getAssembleType() {
		return org.middleheaven.quantity.math.Number.class;
	}

	@Override
	public void assemble(AssemblyLineService service, AssemblyContext context,
			Data data) {
		// TODO implement Assembler.assemble
		
	}

	@Override
	public void unAssemble(AssemblyLineService service,
			AssemblyContext context, Data data) {
	
		org.middleheaven.quantity.math.Number number = (org.middleheaven.quantity.math.Number)data.getValue();
		
		context.put(data.getName(), number.asNumber());
	}

}
