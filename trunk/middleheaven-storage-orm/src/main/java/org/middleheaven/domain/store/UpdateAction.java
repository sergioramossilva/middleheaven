/**
 * 
 */
package org.middleheaven.domain.store;

import java.util.Collections;



class UpdateAction extends StoreAction{

	public UpdateAction (EntityInstance storable){
		super(storable);
	}

	@Override
	public boolean execute(EntityInstanceStorage dataStorage) {
		dataStorage.update(Collections.singleton(this.getStorable()));

		this.getStorable().setStorableState(StorableState.RETRIVED);
		return true;
	}



}