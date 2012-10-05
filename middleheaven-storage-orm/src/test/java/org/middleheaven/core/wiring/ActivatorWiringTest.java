/**
 * 
 */
package org.middleheaven.core.wiring;

import org.junit.Test;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.domain.store.DomainStoreServiceActivator;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.jndi.JNDINameDirectoryService;
import org.middleheaven.persistance.DataServiceActivator;
import org.middleheaven.persistance.db.datasource.DataSourceServiceActivator;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.transactions.AutoCommitTransactionServiceActivator;


/**
 * 
 */
public class ActivatorWiringTest extends MiddleHeavenTestCase{

	
	
	@Test
	public void testInit(){
		
		this.getWiringService().addConfiguration(new BindConfiguration (){

			@Override
			public void configure(Binder binder) {

			
				binder.bind(NameDirectoryService.class).in(Shared.class).to(JNDINameDirectoryService.class);
				binder.bind(DataSourceServiceActivator.class).in(Shared.class).to(DataSourceServiceActivator.class);
				binder.bind(DataServiceActivator.class).in(Shared.class).to(DataServiceActivator.class);
				binder.bind(DomainStoreServiceActivator.class).in(Shared.class).to(DomainStoreServiceActivator.class);
				// the AutoCommitTransactionServiceActivator installs the TransactionService needed by the other activators. 
				// this must work independently of the binding order
				binder.bind(AutoCommitTransactionServiceActivator.class).in(Shared.class).to(AutoCommitTransactionServiceActivator.class);
			}

		});
		
	}
}
