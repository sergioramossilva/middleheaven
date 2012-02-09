/**
 * Data: 29/09/2008
 *
 * Copyright 2008, BRISA
 * 
 */
package org.middleheaven.persistance.db.metamodel;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.sql.DataSource;

import org.middleheaven.sequence.DefaultToken;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.sequence.SequenceToken;

public class TableBasedSequence implements Sequence<Long> {

	private String name;
	private long current = 0;;
	private DataSource ds;
	
	private Set<TableBaseSequenceListener> listeners = new CopyOnWriteArraySet<TableBaseSequenceListener>();
	
	public TableBasedSequence(DataSource ds,String name) {
		super();
		this.name = name;
		this.ds = ds;
	}

	public DataSource getDataSource(){
		return ds;
	}
	
	public String getName(){
		return name;
	}
	
	public void setLastUsed(int lastUsed){
		this.current = lastUsed;
	}
	
	public long getLastUsed(){
		return this.current;
	}
	
	@Override
	public SequenceToken<Long> next() {
		current++;
		fireEvent(current);
		return new DefaultToken<Long>(current);
	}
	
	private void fireEvent(long lastUsed){
		for (TableBaseSequenceListener listener : listeners){
			listener.onSequenceFoward(this);
		}
	}
	
	public void addSequenceListener(TableBaseSequenceListener listener){
		listeners.add(listener);
	}
}