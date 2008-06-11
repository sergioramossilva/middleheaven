/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.util.sequence.service;

import org.middleheaven.util.sequence.StatePersistentSequence;


public interface SequenceStorageService {

    public void store(StatePersistentSequence<?> sequence);
    public String retriveLastValue(StatePersistentSequence<?> sequence);
}
