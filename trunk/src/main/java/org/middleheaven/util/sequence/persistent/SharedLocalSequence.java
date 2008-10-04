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
import org.middleheaven.util.sequence.AbstractStatePersistanteSequence;
import org.middleheaven.util.sequence.SequenceState;
import org.middleheaven.util.sequence.SequenceStateListener;
import org.middleheaven.util.sequence.SequenceToken;
import org.middleheaven.util.sequence.SequenceAdapter;
import org.middleheaven.util.sequence.StateEditableSequence;
import org.middleheaven.util.sequence.StatePersistentSequence;


/**
 * @author  Sergio M. M. Taborda 
 */
public class SharedLocalSequence<T extends Comparable<? super T>> extends AbstractStatePersistanteSequence<T>  {

	private Object lastUsed;
	private StateEditableSequence<T> baseSequence;
	
	ReentrantLock lock = new ReentrantLock();
    BlockingQueue<SharedLocalSequenceValue> queue = new PriorityBlockingQueue<SharedLocalSequenceValue>();
    SharedLocalSequenceValue sv;

    public static <K extends Comparable<? super K>> SharedLocalSequence<K> getSequence(String name,StatePersistentSequence<K> baseSequence){
        return new SharedLocalSequence<K>(name,baseSequence);
    }
    
	private SharedLocalSequence(String name,StateEditableSequence<T> sequence) {
		super(name);
		this.baseSequence = sequence;
	}
    
	@Override
	public SequenceState getSequenceState() {
		return new SequenceState(this.getName(),lastUsed);
	}

	@Override
	public void setSequenceState(SequenceState state) {
		this.lastUsed =  state.getLastUsedValue();
		this.baseSequence.setSequenceState(state);
	}
 
    public SequenceToken<T> next() {
        
        SharedLocalSequenceValue<T> sv = new SharedLocalSequenceValue<T>(baseSequence.next().getValue());
        queue.add(sv);
        
        // enlist as XAResource 
        TransactionService service = ServiceRegistry.getService(TransactionService.class);
        service.enlistResource(sv); // occurs START
      
        
        return sv;
    }

    private class SharedLocalSequenceValue<T> implements SequenceToken<T>,Comparable<SharedLocalSequenceValue<T>>, XAResource {

        private Object actualValue;
        private Xid xid;
        
        SharedLocalSequenceValue(T actualValue){
            this.actualValue = actualValue;
        }

        public int compareTo(SharedLocalSequenceValue other) {
            return (((Comparable)this.actualValue).compareTo(((Comparable)other.actualValue)));
        }
        
        public String toString(){
            return actualValue.toString();
        }
        
        public T getValue() {

            while (queue.peek()!=this){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                   return null;
                }
            }
            
            return (T)actualValue;
 
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
            
            fireStateChange(new SequenceState(getName(),lastUsed));

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

       

 

    }





}
