package org.middleheaven.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

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

}
