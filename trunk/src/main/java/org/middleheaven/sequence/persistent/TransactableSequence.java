/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.sequence.persistent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.sequence.AbstractStatePersistanteSequence;
import org.middleheaven.sequence.SequenceState;
import org.middleheaven.sequence.SequenceToken;
import org.middleheaven.sequence.StateEditableSequence;
import org.middleheaven.sequence.StatePersistentSequence;
import org.middleheaven.transactions.TransactionService;
import org.middleheaven.transactions.XAResourceAdapter;


/**
 * @author  Sergio M. M. Taborda 
 */
public class TransactableSequence<T extends Comparable<? super T>> extends AbstractStatePersistanteSequence<T>  {

	private Object lastUsed;
	private StateEditableSequence<T> baseSequence;
	
    BlockingQueue<TransactableSequenceValue<T>> queue = new PriorityBlockingQueue<TransactableSequenceValue<T>>();


    public static <K extends Comparable<? super K>> TransactableSequence<K> getSequence(String name,StatePersistentSequence<K> baseSequence){
        return new TransactableSequence<K>(name,baseSequence);
    }
    
	private TransactableSequence(String name,StateEditableSequence<T> sequence) {
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
        
        TransactableSequenceValue<T> sv = new TransactableSequenceValue<T>(baseSequence.next().value());
        queue.add(sv);
        
        // enlist as XAResource 
        TransactionService service = ServiceRegistry.getService(TransactionService.class);
        service.enlistResource(sv); // occurs START
      
        
        return sv;
    }

    private class TransactableSequenceValue<I extends Comparable<? super I>> extends XAResourceAdapter implements SequenceToken<I>,Comparable<TransactableSequenceValue<I>>, XAResource {

        private I actualValue;
        
        TransactableSequenceValue(I actualValue){
            this.actualValue = actualValue;
        }

        public int compareTo(TransactableSequenceValue<I> other) {
            return this.actualValue.compareTo(other.actualValue);
        }
        
        public String toString(){
            return actualValue.toString();
        }
        
        public I value() {

            while (queue.peek()!=this){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                   return null;
                }
            }
            
            return actualValue;
 
        }

        public synchronized void commit(Xid xid, boolean onePhase) throws XAException {
            // do nothing
            lastUsed = this.actualValue;
            queue.remove(this);
            
            fireStateChange(new SequenceState(getName(),lastUsed));

        }

        public synchronized void rollback(Xid xid) throws XAException {
            
              // TODO
               
        }

    }





}
