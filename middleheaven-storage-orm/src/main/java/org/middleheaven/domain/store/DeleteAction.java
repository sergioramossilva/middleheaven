/**
 * 
 */
package org.middleheaven.domain.store;

import java.util.Collections;




class DeleteAction extends StoreAction{

	public DeleteAction (EntityInstance storable){
		super(storable);
	}

	
	
	@Override
	public boolean execute(EntityInstanceStorage dataStorage) {
		dataStorage.remove(Collections.singleton(this.getStorable()));

		getStorable().setStorableState(StorableState.DELETED);
		return true;
	}


}