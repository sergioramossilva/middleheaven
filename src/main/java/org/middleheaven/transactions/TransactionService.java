package org.middleheaven.transactions;

import javax.transaction.xa.XAResource;


public interface TransactionService {

	void enlistResource(XAResource sv);

}
