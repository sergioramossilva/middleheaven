/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.util.sequence.persistent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.transactions.TransactionService;
import org.middleheaven.util.sequence.SequenceToken;


/**
 * @author  Sergio M. M. Taborda 
 */
public class SharedLocalLongSequence extends StorageServiceStatePersistentSequence<Long>  {

    long lastUsed=0;
    long current=0;
    BlockingQueue<SharedLocalSequenceValue> queue = new PriorityBlockingQueue<SharedLocalSequenceValue>();
    ReentrantLock lock = new ReentrantLock();
    SharedLocalSequenceValue sv;

    public static SharedLocalLongSequence getSequence(String name){
        return new SharedLocalLongSequence(name);
    }
    
    private SharedLocalLongSequence (String name){
        super(name);
    }
    
    @Override
    protected void inicializeLast(String lastUsed) {
        this.lastUsed = lastUsed== null? 0: Long.parseLong(lastUsed);
        this.current = this.lastUsed+1;
    }
    
    public Long lastUsedValue() {
        return lastUsed;
    }
    
 
    public SequenceToken<Long> next() {
        
        SharedLocalSequenceValue sv = new SharedLocalSequenceValue(current++);
        queue.add(sv);
        
        // enlist as XAResource 
        TransactionService service = ServiceRegistry.getService(TransactionService.class);
        service.enlistResource(sv); // occurs START
      
        
        return sv;
    }

    private class SharedLocalSequenceValue implements SequenceToken<Long>,Comparable<SharedLocalSequenceValue>, XAResource {

        private long actualValue;
        private Xid xid;
        SharedLocalSequenceValue(long actualValue){
            this.actualValue = actualValue;
        }

        public String toString(){
            return Long.toString(actualValue);
        }
        
        public Long value() {

            while (queue.peek()!=this){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                   return null;
                }
            }
            
            return actualValue;
 
        }

        
        public synchronized void start(Xid xid, int flag) throws XAException {
            // Associate to transaction  
            this.xid = xid;
        }

        public synchronized void end(Xid xid, int flag) throws XAException {
            // Disassociate from transaction
            xid = null;
        }

        public synchronized int prepare(Xid xid) throws XAException {
            return XA_OK;
        }

        public synchronized void commit(Xid xid, boolean onePhase) throws XAException {
            // do nothing
            lastUsed = this.actualValue;
            queue.remove(this);
            persist();
        }

        public synchronized void rollback(Xid xid) throws XAException {
            
                queue.remove(this);
                current= lastUsed+1;
                
                for (SharedLocalSequenceValue s : queue){
                    s.actualValue = current;
                    current++;
                }
               
        }

        public void forget(Xid xid) throws XAException {
            // Heuristic support - n/a

        }

        public int getTransactionTimeout() throws XAException {
            return 0;
        }

        public boolean setTransactionTimeout(int arg0) throws XAException {
            return false;
        }

        public boolean isSameRM(XAResource xa) throws XAException {
            return this == xa;
        }

        public Xid[] recover(int flag) throws XAException {
            return new Xid[]{xid};
        }

        public int compareTo(SharedLocalSequenceValue other) {
            return (int)(this.actualValue - other.actualValue);
        }

 

    }




}
