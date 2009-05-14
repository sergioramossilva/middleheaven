package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.NegationClassifier;

public class EncancedListAdapter<T> extends EnhancedCollectionAdapter<T> implements EnhancedList<T> {

	public EncancedListAdapter(List<T> original) {
		super(original);
	}
	
	@Override
	public EnhancedList<T>  reject(Classifier<Boolean, T> classifier){
		return findAll(new NegationClassifier<T>(classifier));
	}
	
	@Override
	public EnhancedList<T>  findAll(Classifier<Boolean, T> classifier) {
		EnhancedList<T> result = CollectionUtils.enhance(new LinkedList<T>());
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = classifier.classify(o);
			if (b!=null && b.booleanValue()){
				result.add(o);
			}
		}
		return result;
	}
	
	protected List<T> original(){
		return (List<T>) super.original();
	}

	@Override
	public EnhancedList<T> subList(int a, int b) {
		return new EncancedListAdapter<T>(this.subList(a, b));
	}

	@Override
	public void add(int index, T element) {
		original().add(index , element);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return original().addAll(index , c);
	}

	@Override
	public T get(int index) {
		return original().get(index);
	}

	@Override
	public int indexOf(Object o) {
		return original().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return original().lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return original().listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return original().listIterator(index);
	}

	@Override
	public T remove(int index) {
		return original().remove(index);
	}

	@Override
	public T set(int index, T element) {
		return original().set(index, element);
	}
	
	@Override
	public T random() {
		return this.get(( int )( Math.random() * this.size()));
	}

	@Override
	public T random(Random random) {
		return this.get(random.nextInt(this.size()));
	}

}
