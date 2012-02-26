/**
 * 
 */
package org.middleheaven.core.wiring;

import org.junit.Test;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.domain.store.DomainStoreServiceActivator;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.jndi.JNDINameDirectoryService;
import org.middleheaven.persistance.DataPersistanceServiceActivator;
import org.middleheaven.persistance.db.datasource.DataSourceServiceActivator;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.transactions.AutoCommitTransactionServiceActivator;
import org.middleheaven.transactions.TransactionService;


/**
 * 
 */
public class ActivatorWiringTest extends MiddleHeavenTestCase{

	
	
	@Test
	public void testInit(){
		
		this.getWiringService().getObjectPool().addConfiguration(new BindConfiguration (){

			@Override
			public void configure(Binder binder) {

			
				binder.bind(NameDirectoryService.class).to(JNDINameDirectoryService.class).in(Shared.class);
				binder.bind(DataSourceServiceActivator.class).to(DataSourceServiceActivator.class).in(Shared.class);
				binder.bind(DataPersistanceServiceActivator.class).to(DataPersistanceServiceActivator.class).in(Shared.class);
				binder.bind(DomainStoreServiceActivator.class).to(DomainStoreServiceActivator.class).in(Shared.class);
				// the AutoCommitTransactionServiceActivator installs the TransactionService needed by the other activators. 
				// this must work independently of the binding order
				binder.bind(AutoCommitTransactionServiceActivator.class).to(AutoCommitTransactionServiceActivator.class).in(Shared.class);
			}

		});
		
	}
}
