/**
 * 
 */
package org.middleheaven.storage;

import java.util.Collections;

class InsertAction extends StoreAction{

	public InsertAction (Storable storable){
		super(storable);
	}

	@Override
	public boolean execute(DataStorage dataStorage) {
		dataStorage.insert(Collections.singleton(storable));

		storable.setStorableState(StorableState.RETRIVED);
		
		return true;
	}

}