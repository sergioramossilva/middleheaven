package org.middleheaven.storage;

import java.util.LinkedList;
import java.util.List;

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
	public void commitTo(DataStorage dataStorage) {
		for (StoreAction action : actions) {
			action.execute(dataStorage);
		}
	}

	@Override
	public void roolback() {
		actions.clear();
	}

}
