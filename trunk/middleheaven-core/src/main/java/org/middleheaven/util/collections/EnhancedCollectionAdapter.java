package org.middleheaven.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.middleheaven.util.classification.Classification;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.MapClassification;
import org.middleheaven.util.classification.NegationClassifier;

public class EnhancedCollectionAdapter<T> extends AbstractEnumerableAdapter<T> implements EnhancedCollection<T> , EnhancedSet<T> {

	private Collection<T> original;

	protected EnhancedCollectionAdapter(Collection<T> original){
		this.original = original;
	}

	public String toString(){
		return original.toString();
	}
	
	
	protected Collection<T> original(){
		return original;
	}

	@Override
	public boolean add(T e) {
		return original.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return original.addAll(c);
	}

	@Override
	public void clear() {
		original.clear();
	}

	@Override
	public boolean contains(Object o) {
		return original.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return original.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return original.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return original.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return original.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return original.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return original.retainAll(c);
	}

	@Override
	public int size() {
		return original.size();
	}

	@Override
	public Object[] toArray() {
		return original.toArray();
	}

	@Override
	public <O> O[] toArray(O[] a) {
		return original.toArray(a);
	}
	
	@Override
	public EnhancedCollection<T> union(EnhancedCollection<T> other) {
		EnhancedCollection<T> result = CollectionUtils.enhance(new ArrayList<T>(size() + other.size()));

		result.addAll(original);
		result.addAll(other);

		return result;
	}

	@Override
	public EnhancedCollection<T> intersect(EnhancedCollection<T> other) {
		return CollectionUtils.enhance(CollectionUtils.intersect(original, other));
	}
	
	
	@Override
	public EnhancedCollection<T> shuffle() {
		ArrayList<T> result = new ArrayList<T>(original);
		Collections.shuffle(result);
		return CollectionUtils.enhance(result);
	}

	@Override
	public EnhancedCollection<T> shuffle(Random random) {
		ArrayList<T> result = new ArrayList<T>(original);
		Collections.shuffle(result,random);
		return CollectionUtils.enhance(result);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EnhancedList<T> sort() {
		if(isEmpty()){
			return CollectionUtils.enhance(new ArrayList<T>());
		} 

		T first = iterator().next();
		ArrayList result = new ArrayList(original);
		if(first instanceof Comparable){
			Collections.sort(result);
		}
		return CollectionUtils.enhance(result);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EnhancedList<T> sort(Comparator<? super T> comparator) {
		if(isEmpty()){
			return CollectionUtils.enhance(new ArrayList<T>());
		} 

		Comparator<Object> xcom = (Comparator<Object>) comparator;
		ArrayList<Object> result = new ArrayList<Object>(original);
		Collections.sort(result, xcom);
		return (EnhancedList<T>) CollectionUtils.enhance(result);

	}
	
	@Override
	public T random() {
		return get(( int )( Math.random() * this.size()));
	}

	private T get(int index){
		Iterator<T> it = this.iterator();
		while (index>0 && it.hasNext()){
			index--;
		}
		return it.next();
	}
	
	@Override
	public T random(Random random) {
		return get(( int )( random.nextInt(this.size())));
	}
	
	@Override
	public <C> Classification<C, T> classify(Classifier<C, T> classifier) {
		MapClassification<C, T> result = new MapClassification<C, T>();
		
		for (T t: this){
			result.addElement(classifier.classify(t), t);
		}
		
		return result;
	}

	@Override
	public boolean containsSame(Collection<T> other) {
		return CollectionUtils.equalContents(this, other);
	}

	@Override
	public EnhancedCollection<T> asSynchronized() {
		return new EnhancedCollectionAdapter<T>(Collections.synchronizedCollection(this.original));
	}

	@Override
	public EnhancedCollection<T> asUnmodifiable() {
		return new EnhancedCollectionAdapter<T>(Collections.unmodifiableCollection(this.original));
	}
	
	public EnhancedList<T> asList(){
		return new EncancedListAdapter<T>(new ArrayList<T>(this.original()));
	}
	
	public EnhancedSet<T> asSet(){
		if (this.original instanceof Set){
			return this;
		}
		return new EnhancedCollectionAdapter<T>( new HashSet<T>(this.original()));
	}

	@Override
	public T getFirst() {
		return original.isEmpty() ? null : original.iterator().next();
	}
}
