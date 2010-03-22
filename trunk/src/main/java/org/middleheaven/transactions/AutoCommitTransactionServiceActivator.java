package org.middleheaven.transactions;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;

public class AutoCommitTransactionServiceActivator extends Activator {

	
	@Publish 
	public AutoCommitTransactionService getTestTransactionService(){
		return new AutoCommitTransactionService();
	}
	
	@Override
	public void activate(ActivationContext context) {}

	@Override
	public void inactivate(ActivationContext context) {}

}
