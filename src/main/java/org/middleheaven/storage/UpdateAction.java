/**
 * 
 */
package org.middleheaven.storage;

import java.util.Collections;

class UpdateAction extends StoreAction{

	public UpdateAction (Storable storable){
		super(storable);
	}

	@Override
	public boolean execute(DataStorage dataStorage) {
		dataStorage.update(Collections.singleton(this.getStorable()));

		this.getStorable().setStorableState(StorableState.RETRIVED);
		return true;
	}



}