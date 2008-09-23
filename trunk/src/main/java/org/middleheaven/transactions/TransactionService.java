package org.middleheaven.transactions;

import javax.transaction.xa.XAResource;


public interface TransactionService {

	/**
	 * Enlist the resource as part of the transaction
	 * @param xaResource the resource
	 */
	void enlistResource(XAResource xaResource);

}
