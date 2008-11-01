package org.middleheaven.storage.db;

import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.middleheaven.util.sequence.NamedSequenceAdapter;
import org.middleheaven.util.sequence.Sequence;
import org.middleheaven.util.sequence.SequenceStateListener;
import org.middleheaven.util.sequence.Sequences;
import org.middleheaven.util.sequence.StateChangedEvent;
import org.middleheaven.util.sequence.StatePersistentSequence;

public abstract class SequenceNotSupportedDialect extends DataBaseDialect implements SequenceStateListener {

	protected SequenceNotSupportedDialect(String startDelimiter,
			String endDelimiter, String fieldSeparator) {
		super(startDelimiter, endDelimiter, fieldSeparator);
	}

	private static Map<String, Sequence> sequences = new TreeMap<String,Sequence>();
	

	public Sequence<Long> getSequence(DataSource datasource, String name){
		Sequence<Long> sequence = sequences.get(name);
		if ( sequence ==null){
			sequence = Sequences.newSequenceFor(Long.class);
			if (!(sequence instanceof StatePersistentSequence)){
				throw new ClassCastException("No valid sequence has found for type " + Long.class);
			} else {
				NamedSequenceAdapter named = new NamedSequenceAdapter(name,((StatePersistentSequence)sequence));
				
				named.addSequenceStateListener(this);
			}
			sequences.put(name, sequence);
		}
		return sequence;
	}

	public void onStateChanged (StateChangedEvent event){
		
	}
}
