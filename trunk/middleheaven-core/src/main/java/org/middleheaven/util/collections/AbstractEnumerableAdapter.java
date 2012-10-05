package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.middleheaven.util.StringUtils;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.NegatedPredicate;
import org.middleheaven.util.classification.Predicate;

/**
 * Base implementation of the Enumerable interface.
 *
 * @param <T> any type.
 */
public abstract class AbstractEnumerableAdapter<T> implements Enumerable<T>{


	@Override
	public boolean any(Predicate<T> predicate) {
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = predicate.classify(o);
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
	public boolean every(Predicate<T> predicate) {
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = predicate.classify(o);
			if (b!=null && !b.booleanValue()){
				return false;
			}
		}
		return true;
	}

	@Override
	public T find(Predicate<T> predicate) {
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = predicate.classify(o);
			if (b!=null && b.booleanValue()){
				return o;
			}
		}
		return null;
	}

	@Override
	public EnhancedCollection<T> findAll(Predicate<T> predicate) {
		EnhancedCollection<T> result = CollectionUtils.enhance(new LinkedList<T>());
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = predicate.classify(o);
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
	public void forEach(Walker<T> walker) {

		for (T t : this){
			walker.doWith(t);
		}

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<T>> L into(L collection) {
		for (T t : this){
			collection.add(t);
		}
		return collection;
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

	@Override
	public EnhancedCollection<T> reject(Classifier<Boolean, T> classifier){
		return findAll(new NegatedPredicate<T>(classifier));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Walkable<T> filter(Predicate<T> predicate) {
		return new IterableWalkable<T>(this).filter(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Walkable<C> map(Classifier<C, T> classifier) {
		return new IterableWalkable<T>(this).map(classifier);
	}



	
}
