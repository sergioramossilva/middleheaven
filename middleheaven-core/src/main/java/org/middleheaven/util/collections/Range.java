package org.middleheaven.util.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.middleheaven.util.Hash;
import org.middleheaven.util.Incrementable;
import org.middleheaven.util.IncrementableIncrementor;
import org.middleheaven.util.Incrementor;
import org.middleheaven.util.NumberIncrementor;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.NegationClassifier;


/**
 * Provides range semantics and operations.
 * A {@link Range} is like an {@link Interval} with iterator semantics over the interval values.
 * An {@link Incrementor} is used to define how the iteration visits the values in the interval.
 * 
 * @param <T> the type of the value in the range.
 */
public class Range<T> extends Interval<T> implements Enumerable<T> , RandomEnumerable<T> {


	private Incrementor<? super T> incrementor;

	/**
	 * An empty {@link Range}.
	 * @return An empty {@link Range}
	 */
	public static <V> Range<V> emptyRange(){
		return new Range<V>(new ComparableComparator<V>(), EmptyIncrementor.emptyIncrementor());
	}

	/**
	 * An empty {@link Range} using a specified comparator.
	 * @param comparator the comparator to use
	 * @return An empty {@link Range}
	 */
	public static <V> Range<V> emptyRange(Comparator<? super V> comparator){
		return new Range<V>(comparator,EmptyIncrementor.emptyIncrementor());
	}

	/**
	 * Comparability method to enable simple use with <code>Integer</code> and other <code>Number</code>
	 * @param <N>
	 * @param start start of the range
	 * @param end end of the range
	 * @param increment the iteration increment from start to end
	 * @return
	 */
	public static <N extends java.lang.Number> Range<N> over(N start, N end , N increment){
		if (start == null || end == null || increment ==null){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		return new Range<N>(start,end,new ComparableComparator<N>(),new NumberIncrementor(increment));
	}



	/**
	 * A {@link Range} from <code>start</code> to <code>end</code>
	 * 
	 * @param start the lower limit of the range
	 * @param end the upper limit of the range
	 * @return A {@link Range} from <code>start</code> to <code>end</code>
	 */
	public static <N extends java.lang.Number> Range<N> over(N start, N end){
		if (start == null || end == null){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		return new Range<N>(start,end,new ComparableComparator<N>(),new NumberIncrementor(Long.valueOf(1)));
	}
	
	/**
	 * Creates a Range from the values of two objects and a <code>Comparator</code> for those objects.
	 * @param <V>
	 * @param start start of the range
	 * @param end end of the range
	 * @param comparator <code>Comparator</code> encapsulating the rules of order for the passed object class
	 * @return a <code>Range</code> from <code>start</code> to <code>end</code>
	 */
	public static <V extends Incrementable<V>> Range<V> over(V start, V end , V increment){
		if (start == null || end == null || increment==null){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		Incrementor<V> incrementor = new IncrementableIncrementor<V>(increment);
	
		return over(start,end,new ComparableComparator<V>(),incrementor);
	
	}
	
	/**
	 * A {@link Range} from <code>start</code> to <code>end</code>
	 * 
	 * @param start the lower limit of the range
	 * @param end the upper limit of the range
	 * @param incrementor the incrementor to use.
	 * @return A {@link Range} from <code>start</code> to <code>end</code>
	 */
	public static <V> Range<V> overIncrementor( V start, V end, Incrementor<? super V> incrementor){
		if (start == null || end == null || incrementor==null){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		return new Range<V>(start,end,new ComparableComparator<V>(),incrementor);
	}
	
	/**
	 * Creates a Range from the values of two objects and a <code>Comparator</code> for those objects.
	 * @param <V>
	 * @param start start of the range
	 * @param end end of the range
	 * @param comparator <code>Comparator</code> encapsulating the rules of order for the passed object class.
	 * @param incrementor the incrementor for <V> type.
	 * @return a <code>Range</code> from <code>start</code> to <code>end</code>
	 */
	public static <V> Range<V> over(V start, V end, Comparator<V> comparator, Incrementor<V> incrementor  ){
		if (comparator == null){
			throw new IllegalArgumentException("A comparator is required.");
		}
		if (comparator.compare(start,end)>0){
			throw new IllegalArgumentException("Range`s start must preceed its end");
		}

		return new Range<V>(start,end,comparator,incrementor);
	} 


	Range(T start, T end,Comparator<? super T> comparator,Incrementor<? super T> incrementor) {
		super(start,end,comparator);
		this.incrementor = incrementor;
	}

	/**
	 * @param start
	 * @param end
	 * @param comparator
	 */
	Range(Comparator<? super T> comparator,Incrementor<? super T> incrementor) {
		super(comparator);
		this.incrementor = incrementor;
	}

	/**
	 * Determines this interval intersection with another
	 * @param other the interval to intersect
	 * @return an interval representing the intersections of the intervals
	 */
	public Range<T> intersection (Interval<T> other){

		// is one is empty the intersection is empty
		if (this.isEmpty() || other.isEmpty()) {
			return new Range<T>(this.comparator,this.incrementor);
		}

		if (other.equals(this)){ // are the same. 
			return this;
		}

		if (this.intersects(other)) {
			// intersects
			return new Range<T>( max(this.start, other.start), min(this.end, other.end) , comparator, incrementor);
		} else {
			// does not intersect
			return new Range<T>(this.comparator,incrementor);
		}

	}

	/**
	 * The union of this range with an another. 
	 * The result is a range from the lowest start to the greatest end
	 * Note that union is a commutative operation,
	 * meaning that R.union(S).equals(S.union(R)) == true , R and S being instances of Range.
	 * @param other
	 * @return a range from the lowest start to the greatest end. Example : [2,3] merged with [7,10]  will return [2,10] 
	 */
	public Range<T> union (Interval<T> other){
		return new Range<T> ( 
				min(this.start, other.start) , // start at the minor start
				max(this.end , other.end), // end at the major end
				this.comparator, // the same comparator
				this.incrementor
		);
	}


	/**
	 * Ranges are equal if the their starts are equal and their ends are equals
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object other){
		if (other instanceof Range){
			return this.equalsRange((Range)other);
		} else if (other instanceof Interval){
			return this.equalsInterval((Interval)other);
		} else {
			return false;
		}
	}

    public int hashCode(){
    	return Hash.hash(start).hash(end).hashCode();
    }
	/**
	 * Ranges are equal if their starts are equal and their ends are equals
	 */
	private boolean equalsRange(Range<T> other){
		return (this.isEmpty() && other.isEmpty() ) || // both are empty or the limits are equal
		( comparator.compare(start, other.start)==0 && comparator.compare(end, other.end)==0);
	}
	
	private boolean equalsInterval(Interval<T> other){
		return (this.isEmpty() && other.isEmpty() ) || // both are empty or the limits are equal
		( comparator.compare(start, other.start)==0 && comparator.compare(end, other.end)==0);
	}

	@Override
	public Iterator<T> iterator() {
		return new RangeIterator<T>(start,end,comparator,incrementor);
	}

	/**
	 * Collects all the values in the {@link Range} into an {@link EnhancedList}.
	 * @return a list with the values in the {@link Range}
	 */
	public EnhancedList<T> toList(){
		List<T> list = new LinkedList<T>();
		for (T i : this){
			list.add(i);
		}
		return CollectionUtils.enhance(list);
	}

	private int size = -1;
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int size(){
		if(size>=0){
			return size;
		}
		size=0;
		for (Iterator<T> it = this.iterator();it.hasNext();){
			it.next();
			size++;
		}
		return size;
	}

	static class RangeIterator<S> implements Iterator<S> {

		S current;
		Comparator<? super S> comparator;
		Incrementor<? super S> incrementor;
		S end;


		public RangeIterator(S current, S end, Comparator<? super S> comparator , Incrementor<? super S> incrementor){
			this.current =current;
			this.end = end;
			this.comparator = comparator;
			this.incrementor = incrementor;
		}

		@Override
		public boolean hasNext() {
			return (current != end) && comparator.compare(current, end)<=0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public S next() {
			S toReturn = current;
			current = (S)incrementor.increment(current);
			return toReturn;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public T random() {
		return this.toList().random();
	}

	public T random(Random random) {
		return this.toList().random(random);
	}

	@Override
	public void each(Walker<T> walker) {
		for (T t : this){
			walker.doWith(t);
		}
	}

	@Override
	public boolean any(Classifier<Boolean, T> classifier) {
		return this.toList().any(classifier);
	}

	@Override
	public <C> EnhancedCollection<C> collect(Classifier<C, T> classifier) {
		return this.toList().collect(classifier);
	}

	@Override
	public int count(T object) {
		return this.contains(object) ? 1 : 0;
	}

	@Override
	public boolean every(Classifier<Boolean, T> classifier) {
		return this.toList().every(classifier);
	}

	@Override
	public T find(Classifier<Boolean, T> classifier) {
		return this.toList().find(classifier);
	}

	@Override
	public EnhancedCollection<T> findAll(Classifier<Boolean, T> classifier) {
		return this.toList().findAll(classifier);
	}

	@Override
	public <C> EnhancedMap<C, EnhancedCollection<T>> groupBy(Classifier<C, T> classifier) {
		return this.toList().groupBy(classifier);
	}


	@Override
	public String join(String separator) {
		return StringUtils.join(separator, this);
	}

	@Override
	public EnhancedCollection<T> reject(Classifier<Boolean, T> classifier){
		return findAll(new NegationClassifier<T>(classifier));
	}
	



}

