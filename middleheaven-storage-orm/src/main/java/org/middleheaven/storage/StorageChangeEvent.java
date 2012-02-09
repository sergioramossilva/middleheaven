package org.middleheaven.storage;

import java.io.Serializable;

public class StorageChangeEvent implements Serializable{


	private static final long serialVersionUID = 3977647900684013466L;
	
	private Object instance;
	private boolean removed;
	private boolean added;
	private boolean updated;
	
	public StorageChangeEvent(Object instance, boolean removed, boolean added,boolean updated) {
		super();
		this.instance = instance;
		this.removed = removed;
		this.added = added;
		this.updated = updated;
	}

	public Object getInstance() {
		return instance;
	}

	public boolean isRemoved() {
		return removed;
	}

	public boolean isAdded() {
		return added;
	}

	public boolean isUpdated() {
		return updated;
	}
	

	
}
