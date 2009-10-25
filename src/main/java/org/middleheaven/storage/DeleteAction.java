/**
 * 
 */
package org.middleheaven.storage;

import java.util.Collections;

class DeleteAction extends StoreAction{

	public DeleteAction (Storable storable){
		super(storable);
	}

	@Override
	public boolean execute(DataStorage dataStorage) {
		dataStorage.remove(Collections.singleton(storable));

		storable.setStorableState(StorableState.DELETED);
		return true;
	}

}