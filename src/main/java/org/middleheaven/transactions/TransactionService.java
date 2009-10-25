package org.middleheaven.transactions;

import javax.transaction.xa.XAResource;

import org.middleheaven.core.wiring.service.Service;

@Service
public interface TransactionService {

	/**
	 * Enlist the resource as part of the transaction
	 * @param xaResource the resource
	 */
	void enlistResource(XAResource xaResource);

	Transaction getTransaction();

	boolean haveTransaction();

}
