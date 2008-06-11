/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.util.sequence.persistent;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.util.sequence.DefaultToken;
import org.middleheaven.util.sequence.SequenceToken;

public class AutoIncrementSequence extends StorageServiceStatePersistentSequence<Long> {


    private static Map<String , AutoIncrementSequence> sequences = new HashMap<String , AutoIncrementSequence>();
    
    public synchronized static AutoIncrementSequence getSequence(String name){
        AutoIncrementSequence sequence = sequences.get(name);
        if (sequence==null){
            sequence = new AutoIncrementSequence(name);
            sequences.put(name, sequence);
        }
        return sequence;
    }
    
    long lastUsed=0;
    protected AutoIncrementSequence(String name) {
        super(name);
    }

    @Override
    protected void inicializeLast(String value) {
        lastUsed = value==null || value.isEmpty() ? 0 :  Long.valueOf(value);
    }

    public Long lastUsedValue() {
        return new Long(lastUsed);
    }

    @Override
    public SequenceToken<Long> next() {
        return new DefaultToken<Long>(new Long(++lastUsed));
    }

}
