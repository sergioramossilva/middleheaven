package org.middleheaven.domain.store;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.util.identity.Identity;

public class ArrayStorageUnit implements StorageUnit {

	private final List<StoreAction> actions = new LinkedList<StoreAction>();

	@Override
	public void addAction(StoreAction action) {
		actions.add(action);
	}

	@Override
	public void simplify() {
		// TODO implement ArrayStorageUnit.simplify

	}

	@Override
	public void commitTo(EntityInstanceStorage dataStorage) {
		for (StoreAction action : actions) {
			action.execute(dataStorage);
		}
		actions.clear();
	}

	@Override
	public void roolback() {
		actions.clear();
	}

	@Override
	public Collection<EntityInstance> filter(Collection<EntityInstance> all, Class<?> type) {
		if (actions.isEmpty()){
			return all;
		}
		
		Map<Identity, EntityInstance> result = new LinkedHashMap<Identity,EntityInstance>();
		
		for (EntityInstance s : all){
			result.put(s.getIdentity(), s);
		}
		
		for (StoreAction action : actions){
			if (type.isInstance(action.getStorable())){
				if( action instanceof DeleteAction){
					result.remove(action.getStorable().getIdentity());
				} else {
					result.put(action.getStorable().getIdentity(), action.getStorable());
				}
			}
		}
		
		return result.values();
	}

}
