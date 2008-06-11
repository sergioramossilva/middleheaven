/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.util.sequence;

/**
 * A <code>Sequence</code> who's state is preserved 
 * between uses. 
 * Althought implementations are free to implement the persistence logic
 * as they want this sequence is intended to a
 * strictly increasing sequence;
 * 
 * @author Sergio M. M. Taborda 
 *
 */
public interface StatePersistentSequence<T> extends Sequence<T>, NamedSequence<T>{

    
    /**
     * Persists the state.
     * This method is implementation specific, 
     * but normally involves a call to the Sequence Storage Service
     *
     */
    public abstract void persist();
    
    /**
     * Obtains and returns the last used value for the sequence
     * 
     * @return the last used value for the sequence
     */ 
    public abstract T lastUsedValue();
    
    
    
}
