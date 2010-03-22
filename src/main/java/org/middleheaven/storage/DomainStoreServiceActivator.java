package org.middleheaven.storage;

import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.transactions.TransactionService;

public class DomainStoreServiceActivator extends Activator {

	private HashEntityStoreService storeService;
	private WiringService wiringService;
	
	@Publish
	public HashEntityStoreService getDomainStoreService(){
		return storeService;
	} 
	
	@Wire
	public void setWiringService(WiringService wiringService){
		this.wiringService = wiringService;
	}
	
	@Wire
	public void setTransactionService(TransactionService transactionService){
		// just force the transaction service to be present
	}
	
	@Override
	public void activate(ActivationContext context) {
		storeService = new HashEntityStoreService();
		
		// install an EntityStore provider
		
		wiringService.getObjectPool().addConfiguration( new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				Class cr = EntityStoreResolver.class;
				binder.bind(EntityStore.class).toResolver(cr);
			}
			
		});
	}

	@Override
	public void inactivate(ActivationContext context) {}
}
