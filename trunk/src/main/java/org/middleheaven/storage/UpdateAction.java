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
		dataStorage.update(Collections.singleton(storable));

		storable.setStorableState(StorableState.RETRIVED);
		return true;
	}

}