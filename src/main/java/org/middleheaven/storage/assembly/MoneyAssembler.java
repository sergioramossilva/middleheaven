package org.middleheaven.storage.assembly;

import org.middleheaven.quantity.money.Money;

public class MoneyAssembler implements Assembler {

	@Override
	public Class<?> getAssembleType() {
		return Money.class;
	}


	@Override
	public void assemble(AssemblyLineService service, AssemblyContext context, Data data) {
		
		
		String isoCode = (String)context.take(data.getName() + ".currency");
		Object amount = context.take(data.getName() + ".amount");
		
		if (!(amount instanceof Number)){
			
		}
		data.setValue(Money.money((Number)amount, isoCode));
	}

	@Override
	public void unAssemble(AssemblyLineService service,
			AssemblyContext context, Data data) {
		
		Money money = (Money)data.getValue();
		
		context.put(data.getName() + ".currency", money.unit());
		context.put(data.getName() + ".amount", money.amount());
	}

}
