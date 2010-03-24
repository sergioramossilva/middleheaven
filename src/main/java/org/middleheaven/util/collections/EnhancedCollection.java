package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.Random;

import org.middleheaven.util.classification.Classification;
import org.middleheaven.util.classification.Classifier;

public interface EnhancedCollection<T> extends Collection<T> , Enumerable<T> {

	public EnhancedCollection<T> asUnmodifiable();
	
	public EnhancedCollection<T> asSynchronized();
	
	public T random(Random random);

	public T random();
	
	public EnhancedList<T>  sort();

	public EnhancedCollection<T> shuffle();
	public EnhancedCollection<T> shuffle(Random random);
	
	public EnhancedCollection<T> intersect(EnhancedCollection<T> other);
	
	public EnhancedCollection<T> union(EnhancedCollection<T> other);
	
	public EnhancedList<T> sort(Comparator<? super T> comparator);
	
	public <C> Classification<C,T> classify(Classifier<C,T> classifier);

	public boolean containsSame(Collection<T> other);

	public EnhancedList<T> asList();
	public EnhancedSet<T> asSet();

	public T getFist();
}
