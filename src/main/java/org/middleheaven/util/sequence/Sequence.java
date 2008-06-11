/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.util.sequence;


/**
 * Interface for all sequences. 
 * Sequences generate the next token in a series.
 * 
 * Sequences are unlimited. Limied sequences must implement <code>LimitedSequence</code>
 * that acts like a iterator.
 * @author Sergio M. M. Taborda 
 *
 */
public interface Sequence<T> {

    /**
     * Return the next token in the sequence
     * @return  the next token in the sequence
     */
    public abstract SequenceToken<T> next();
}
