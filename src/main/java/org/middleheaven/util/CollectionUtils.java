package org.middleheaven.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

import org.middleheaven.core.wiring.annotations.Wire;

public class CollectionUtils {


	public static <T> List<T> toRandomAccessList(Collection<T> collection){
		if(collection instanceof RandomAccess && collection instanceof List && !collection.getClass().getName().toLowerCase().contains("unmodifiable")){
			return (List<T>)collection;
		}
		
		return new ArrayList<T>(collection);
	}
	
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

	/**
	 * Collections are equals if they contain the same elements 
	 * independent of ordering.
	 * To this to be true, they must have the same number of elements.
	 * Empty collections are equal.
	 * 
	 * @param <T>
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static <T> boolean equalContents(Collection<? extends T> c1,Collection<? extends T> c2) {

		if (c1==c2){
			return true;
		} else if (c1.isEmpty() && c2.isEmpty()){
			return true;
		} if (c1.size()!=c2.size()){
			return false;
		}
	
		
		if (!(c2 instanceof Set) && (c1 instanceof Set || c1.size() > c2.size())) {
			//swap
			Collection<? extends T> tmp = c1;
			c1 = c2;
			c2 = tmp;
		}

		for (T t : c1){
			if (!c2.contains(t)){
				return false;
			}
		}
		return true;

	}

	public static <K,V> boolean equals(Map<? extends K, ? extends V> a,Map<? extends K, ? extends V> b) {
		if (a==b){
			return true;
		} else {
			return equalContents(a.entrySet(), b.entrySet());
		}

	}

	/**
	 * Returns a collection with the common elements in c1 and c2
	 * @param <T>
	 * @param c1
	 * @param c2
	 * @return
	 */
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

		Collection<T> result = new HashSet<T>();
		for (T obj : c1){
			if (c2.contains(obj)){
				result.add(obj);
			}
		}
		
		return result;
	}


	
	public static <T> T[] addToArray(T[] array,T element , T ... elements) {
		
		Class<?> componentType = array.getClass().getComponentType();
		
		Object newArray = Array.newInstance(componentType, array.length+1+elements.length);
		
		System.arraycopy(array, 0, newArray, 0, array.length);
		Array.set(newArray, array.length , element);
		System.arraycopy(elements, 0, newArray, array.length +1,elements.length);
		
		return (T[]) newArray;
	}

	public static <T> T[] newArray(Class<T> arrayType, int length) {
		
		Object newArray = Array.newInstance(arrayType, length);
		return (T[]) newArray;
	}



}
