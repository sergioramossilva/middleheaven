/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.util.sequence.service;

import org.middleheaven.util.sequence.SequenceState;
import org.middleheaven.util.sequence.SequenceStateListener;
import org.middleheaven.util.sequence.StatePersistentSequence;


public interface SequenceStorageService extends SequenceStateListener{

    public void store(SequenceState state);
    public void restore(StatePersistentSequence<?> sequence);

}
