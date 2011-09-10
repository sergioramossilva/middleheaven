package org.middleheaven.util.collections;

import java.util.List;


public interface EnhancedList<T> extends List<T>, Walkable<T>, EnhancedCollection<T>{

	
	public EnhancedList<T> subList(int a, int b);
}
