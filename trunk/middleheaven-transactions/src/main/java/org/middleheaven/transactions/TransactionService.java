package org.middleheaven.transactions;

import javax.transaction.xa.XAResource;

import org.middleheaven.core.annotations.Service;

@Service
public interface TransactionService {

	/**
	 * Enlist the resource as part of the transaction
	 * @param xaResource the resource
	 */
	void enlistResource(XAResource xaResource);

	/**
	 * Obtains the current transaction
	 * @return the current transaction
	 */
	Transaction getTransaction();

	/**
	 * 
	 * @return 
	 */
	boolean isTransactional();

}
