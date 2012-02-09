/**
 * 
 */
package org.middleheaven.domain.store;

import java.util.Collections;




class InsertAction extends StoreAction{

	public InsertAction (EntityInstance storable){
		super(storable);
	}

	@Override
	public boolean execute(EntityInstanceStorage dataStorage) {
		dataStorage.insert(Collections.singleton(this.getStorable()));

		getStorable().setStorableState(StorableState.RETRIVED);
		
		return true;
	}


}