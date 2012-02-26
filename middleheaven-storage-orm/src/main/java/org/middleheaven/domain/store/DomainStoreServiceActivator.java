package org.middleheaven.domain.store;

import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.transactions.TransactionService;

public class DomainStoreServiceActivator extends Activator {

	private HashDomainStoreService storeService;
	private WiringService wiringService;
	
	@Publish
	public DomainStoreService getDomainStoreService(){
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
	public void activate() {
		storeService = new HashDomainStoreService();
		
		// install an EntityStore provider
		
		wiringService.getObjectPool().addConfiguration( new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				Class cr = DomainStoreResolver.class;
				binder.bind(DomainStore.class).toResolver(cr);
			}
			
		});
	}

	@Override
	public void inactivate() {}
}
