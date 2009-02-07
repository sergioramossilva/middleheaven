package org.middleheaven.storage.assembly;

import org.middleheaven.util.measure.money.Money;

public class MoneyAssembler implements Assembler {

	@Override
	public void assemble(DataContext context, AssemblyLine line) {
		// TODO implement MoneyAssembler.assemble

	}

	@Override
	public Class<?> getAssembleType() {
		return Money.class;
	}

	@Override
	public void unAssemble(DataContext context, AssemblyLine line) {
		Money money = (Money)line.getObject();
		
		context.put(line.getName() + ".currency", money.unit());
		context.put(line.getName() + ".amount", money.amount());
	}

}
