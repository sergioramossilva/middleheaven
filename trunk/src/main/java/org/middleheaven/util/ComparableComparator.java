package org.middleheaven.util;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public class  ComparableComparator<T> implements Comparator<T> {


	@Override
	public int compare(T a, T b) {
		return ((Comparable)a).compareTo(b);
	}



}
