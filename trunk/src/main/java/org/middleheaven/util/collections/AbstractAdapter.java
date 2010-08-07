package org.middleheaven.util.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.middleheaven.util.StringUtils;
import org.middleheaven.util.classification.Classifier;

/**
 * Base implementation of the Enumerable interface.
 *
 * @param <T>
 */
public abstract class AbstractAdapter<T> implements Enumerable<T>{


	@Override
	public boolean any(Classifier<Boolean, T> classifier) {
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = classifier.classify(o);
			if (b!=null && b.booleanValue()){
				return true;
			}
		}
		return false;
	}

	@Override
	public <C> EnhancedCollection<C> collect(Classifier<C, T> classifier) {
		EnhancedCollection<C> result = CollectionUtils.enhance(new LinkedList<C>());
		for (Iterator<T> it = iterator();it.hasNext();){
			final C item = classifier.classify(it.next());
			if (item!=null){
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public boolean every(Classifier<Boolean, T> classifier) {
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = classifier.classify(o);
			if (b!=null && !b.booleanValue()){
				return false;
			}
		}
		return true;
	}

	@Override
	public T find(Classifier<Boolean, T> classifier) {
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = classifier.classify(o);
			if (b!=null && b.booleanValue()){
				return o;
			}
		}
		return null;
	}

	@Override
	public EnhancedCollection<T> findAll(Classifier<Boolean, T> classifier) {
		EnhancedCollection<T> result = CollectionUtils.enhance(new LinkedList<T>());
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = classifier.classify(o);
			if (b!=null && b.booleanValue()){
				result.add(o);
			}
		}
		return result;
	}

	@Override
	public final <C> EnhancedMap<C, EnhancedCollection<T>> groupBy(Classifier<C, T> classifier) {
		Map<C, EnhancedCollection<T>> result = new HashMap<C,EnhancedCollection<T>>();
		for (Iterator<T> it = iterator();it.hasNext();){
			T object = it.next();
			
			EnhancedCollection<T> items = result.get(classifier.classify(object));
			
			if (items == null){
				items = new EnhancedArrayList<T>();
			}
			
			items.add(object);
		}
		return CollectionUtils.enhance(result);
	}

	@Override
	public final String join(String separator) {
		return StringUtils.join(separator, this);

	}

	@Override
	public void each(Walker<T> walker) {

		for (T t : this){
			walker.doWith(t);
		}

	}

	@Override
	public int count(T object) {
		int count=0;
		for (T t : this){
			if (t==null ? object==null : t.equals(object)){
				count++;
			}
		}
		return count;
	}



	



	
}
