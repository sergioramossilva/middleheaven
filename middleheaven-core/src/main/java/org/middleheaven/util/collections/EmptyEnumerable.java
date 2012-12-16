/**
 * 
 */
package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
class EmptyEnumerable<T> implements Enumerable<T> {

	
	@SuppressWarnings("rawtypes")
	private static final EmptyEnumerable ME = new EmptyEnumerable();

	@SuppressWarnings("unchecked")
	public static <X> EmptyEnumerable<X> getInstance(){
		return ME;
	}
	
	private EmptyEnumerable(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return Collections.<T>emptySet().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Block<T> walker) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<T>> L into(L collection) {
		return collection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getFirst() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> sort(Comparator<T> comparable) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> reject(Predicate<T> predicate) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(T object) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(Predicate<T> predicate) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean every(Predicate<T> predicate) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean any(Predicate<T> predicate) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T find(Predicate<T> predicate) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> filter(Predicate<T> predicate) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> map(Mapper<C, T> mapper) {
		return getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> mapAll(Mapper<Enumerable<C>, T> mapper) {
		return getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T reduce(T seed, BinaryOperator<T> operator) {
		return seed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> C mapReduce(C seed, Mapper<Enumerable<C>, T> mapper,
			BinaryOperator<C> operator) {
		return seed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C, P extends Pair<C, Enumerable<T>>> Enumerable<P> groupBy(
			Mapper<C, T> classifier) {
		return getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String join(String separator) {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> sort() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <U> Enumerable<U> cast(Class<U> newType) {
		return ME;
	}

}
