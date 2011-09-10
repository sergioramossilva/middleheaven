package org.middleheaven.storage.assembly;

import org.middleheaven.quantity.money.Currency;
import org.middleheaven.quantity.money.Money;

public class CurrencyAssembler implements Assembler {

	@Override
	public void assemble(AssemblyLineService service, AssemblyContext context,
			Data data) {
		// TODO implement CurrencyAssembler.assemble

	}

	@Override
	public Class<?> getAssembleType() {
		return Currency.class;
	}

	@Override
	public void unAssemble(AssemblyLineService service, AssemblyContext context, Data data) {
		Currency c  = (Currency)data.getValue();
		
		context.put(data.getName(), c.toString());
	}

}
