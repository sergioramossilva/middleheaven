package org.middleheaven.transactions;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.tool.test.TestTransactionService;

public class TestTransactionServiceActivator extends Activator {

	
	@Publish 
	public TestTransactionService getTestTransactionService(){
		return new TestTransactionService();
	}
	
	@Override
	public void activate(ActivationContext context) {}

	@Override
	public void inactivate(ActivationContext context) {}

}
