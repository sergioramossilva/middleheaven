package org.middleheaven.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {


	public static <T> Iterator <T> singleIterator(T object){
		return new SingleIterator<T>(object);
	}

	public static <T> Iterator <T> arrayIterator(T[] array){
		return new ArrayIterator<T>(array);
	}

	private static class SingleIterator<T> implements Iterator<T>{

		T object;
		boolean iterated = false;
		public SingleIterator(T object) {
			this.object = object;
		}

		@Override
		public boolean hasNext() {
			return !iterated;
		}

		@Override
		public T next() {
			iterated =true;
			return object;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public static <T> boolean equals(Collection<? extends T> a,Collection<? extends T> b) {

		if (a==b){
			return true;
		} else if (a.isEmpty() && b.isEmpty()){
			return true;
		} if (a.size()!=b.size()){
			return false;
		}
		Iterator<?> ia = a.iterator();
		Iterator<?> ib =b.iterator();
		for (; (ia.hasNext() & ib.hasNext());){
			if (!ia.next().equals(ib.next())){
				return false;
			}
		}

		return true;
	}

	public static <K,V> boolean equals(Map<? extends K, ? extends V> a,Map<? extends K, ? extends V> b) {
		if (a==b){
			return true;
		} else {
			return equals(a.entrySet(), b.entrySet());
		}

	}

	public static <T> Collection<T> intersect(Collection<T> c1,Collection<T> c2) {

		if (c1.isEmpty() || c2.isEmpty()){
			return Collections.emptySet();
		}
		
		if (!(c2 instanceof Set) && (c1 instanceof Set || c1.size() > c2.size())) {
			//swap
			Collection<T> tmp = c1;
			c1 = c2;
			c2 = tmp;
		}

		Collection<T> m = new LinkedList<T>(c1);
		m.retainAll(c2);  
		return m;
	}


}
