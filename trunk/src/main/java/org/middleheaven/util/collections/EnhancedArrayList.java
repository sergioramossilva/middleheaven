package org.middleheaven.util.collections;

import java.util.ArrayList;
import java.util.Collection;

public class EnhancedArrayList<T> extends EncancedListAdapter<T>{

	public EnhancedArrayList() {
		super(new ArrayList<T>());
	}

	public EnhancedArrayList(Collection<? extends T> other) {
		super(new ArrayList<T>(other));
	}
}
