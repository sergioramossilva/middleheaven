package org.middleheaven.transactions;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;


public class AutoCommitTransactionService implements TransactionService {

	Set<XAResource> xar = new HashSet<XAResource>();
	ThreadLocal<Transaction> local = new ThreadLocal<Transaction>();

	
	public AutoCommitTransactionService(){
	
	}
	
	@Override
	public void enlistResource(XAResource xaResource) {
		xar.add(xaResource);
	}

	@Override
	public Transaction getTransaction() {
		Transaction t  = local.get();
		if (t ==null ){
			t = new MyTransaction();
			local.set(t);
		}
		return t;
	}

	private class LongXid implements Xid {

		long i = System.currentTimeMillis();

		@Override
		public byte[] getBranchQualifier() {
			// TODO implement Xid.getBranchQualifier
			return null;
		}

		@Override
		public int getFormatId() {
			// TODO implement Xid.getFormatId
			return 0;
		}

		@Override
		public byte[] getGlobalTransactionId() {
			// TODO implement Xid.getGlobalTransactionId
			return null;
		}

		public boolean equals(Object other){
			return other instanceof LongXid && i == ((LongXid)other).i;
		}

	}

	private class  MyTransaction implements Transaction{

		Xid xid;
		public MyTransaction(){
			xid = new LongXid();
		}

		@Override
		public void begin() {


			try {
				for (XAResource xa : xar){

					xa.start(xid, 0);

				}

			} catch (XAException e) {
				roolback();
			}

		}

		@Override
		public void commit() {
			try {
				for (XAResource xa : xar){
					xa.prepare(xid);
				}
			
				for (XAResource xa : xar){
					xa.commit(xid, false);
				}

				local.set(null);
			} catch (XAException e) {
				roolback();
			}
		}

		@Override
		public void roolback() {

			for (XAResource xa : xar){
				try {
					xa.rollback(xid);
				} catch (XAException e) {
					// log
				}
				
			}




		};
	}

	@Override
	public boolean isTransactional() {
		return local.get() != null;
	}
}
