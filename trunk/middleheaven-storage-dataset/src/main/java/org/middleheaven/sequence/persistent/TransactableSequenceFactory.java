/**
 * 
 */
package org.middleheaven.sequence.persistent;

import org.middleheaven.sequence.StatePersistentSequence;
import org.middleheaven.transactions.TransactionService;

/**
 * 
 */
public class TransactableSequenceFactory {

	 private TransactionService service;
	
	 public TransactableSequenceFactory (TransactionService service){
		 this.service = service;
	 }
	 
	 public  <K extends Comparable<? super K>> TransactableSequence<K> getSequence(String name,StatePersistentSequence<K> baseSequence){
	        return new TransactableSequence<K>(name,baseSequence, service);
	 }
	    
	 
}
