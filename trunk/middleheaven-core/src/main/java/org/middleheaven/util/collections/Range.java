package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.middleheaven.quantity.math.structure.Ring;
import org.middleheaven.util.CharacterIncrementor;
import org.middleheaven.util.Hash;
import org.middleheaven.util.Incrementor;
import org.middleheaven.util.NaturalIncrementable;
import org.middleheaven.util.NumberIncrementor;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.NegatedPredicate;
import org.middleheaven.util.classification.Predicate;


/**
 * Provides range semantics and operations.
 * A {@link Range} is like an {@link Interval} with iterator semantics over the interval values.
 * An {@link Incrementor} is used to define how the iteration visits the values in the interval.
 * 
 * @param <T> the type of the value in the range.
 */
public class Range<T> implements Enumerable<T> , RandomEnumerable<T> {

	private final Comparator<? super T> comparator;
	private final Incrementor<? super T> incrementor;
	private final T start;
	private final T end;
	private boolean isReversed = false;
	private boolean excludeStart = false;
	private boolean excludeEnd = false;

	
	/**
	 * An empty {@link Range}.
	 * @return An empty {@link Range}
	 */
	public static <V> Range<V> emptyRange(){
		Range<V> empty = new Range<V>(new ComparableComparator<V>(), EmptyIncrementor.emptyIncrementor());
		empty.excludeEnd = true;
		empty.excludeStart = true;
		
		return empty;
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
	 * A {@link Range} from <code>start</code> to <code>end</code>
	 * 
	 * @param start the lower limit of the range
	 * @param end the upper limit of the range
	 * @return A {@link Range} from <code>start</code> to <code>end</code>
	 */
	public static Range<Character> over(Character start, Character end){
		if (start == null || end == null){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		return new Range<Character>(start,end, new ComparableComparator<Character>(), new CharacterIncrementor(1));
	}
	
	public static <V extends NaturalIncrementable<V> & Comparable<? super V>> Range<V> over(V start, V end){
		return new Range<V>(start,end,new ComparableComparator<V>(), new NaturalIncrementableIncrementor<V>());
	}

	
	/**
	 * Creates a Range from the values of two objects and a <code>Comparator</code> for those objects.
	 * @param <V>
	 * @param start start of the range
	 * @param end end of the range
	 * @param comparator <code>Comparator</code> encapsulating the rules of order for the passed object class
	 * @return a <code>Range</code> from <code>start</code> to <code>end</code>
	 */
	public static <V extends Ring<V>> Range<V> over(V start, V end , V increment){
		if (start == null || end == null || increment==null){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		Incrementor<V> incrementor = new RingIncrementor<V>(increment);
	
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
	public static <V> Range<V> over( V start, V end, Incrementor<? super V> incrementor){
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

		return new Range<V>(start,end,comparator,incrementor);
	} 
	

	Range(T start, T end,Comparator<? super T> comparator,Incrementor<? super T> incrementor) {
		
		this.start = start;
		this.end = end;
		this.comparator = comparator;
		
		if (start!= null && end != null && comparator.compare(start,end)>0){
			// the ordering ir reversed, so reverse the incrementor
			incrementor = incrementor.reverse();
			isReversed = true;
		}

		this.incrementor = incrementor;
	}

	/**
	 * @param start
	 * @param end
	 * @param comparator
	 */
	Range(Comparator<? super T> comparator,Incrementor<? super T> incrementor) {
		this(null,null,comparator, incrementor);
	}

	public Range<T> excludeStart(){
		Range<T> range = new Range<T>(this.start, this.end, this.comparator, this.incrementor);
		
		range.excludeStart = true;
		
		return range;
	}
	
	public Range<T> excludeEnd(){
		Range<T> range = new Range<T>(this.start, this.end, this.comparator, this.incrementor);
		
		range.excludeEnd = true;
		
		return range;
	}
	
	/**
	 * Determines this interval intersection with another
	 * @param other the interval to intersect
	 * @return an interval representing the intersections of the intervals
	 */
	public Range<T> intersection (Range<T> other){

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
	 * 
	 * {@inheritDoc}
	 */
	public boolean isEmpty(){
		if (start == end || comparator.compare(start, end) == 0){
			if (this.excludeEnd || this.excludeStart) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean contains(T value, boolean includeStart , boolean includeEnd){
		if (this.isReversed) {
			return !this.isEmpty() 
					&& (start==null ? true : (includeStart?comparator.compare(value, start) <=0: comparator.compare(value, start) <0 )) 
					&& (end == null ? true : (includeEnd?comparator.compare(value, end) >=0:comparator.compare(value, end) >0));
		} else {
			return !this.isEmpty() 
					&& (start==null ? true : (includeStart?comparator.compare(value, start) >=0: comparator.compare(value, start) >0 )) 
					&& (end == null ? true : (includeEnd?comparator.compare(value, end) <=0:comparator.compare(value, end) <0));
		}

	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean contains(T value){
		if (comparator.compare(start, end) ==0 ){
			if (this.excludeEnd || this.excludeStart){
				return false;
			} else {
				return comparator.compare(value, end) ==0; 
			}
			
		} else {
			return contains(value, !this.excludeStart, !this.excludeEnd);
		}
		
	}
	
	/**
	 * The union of this range with an another. 
	 * The result is a range from the lowest start to the greatest end
	 * Note that union is a commutative operation,
	 * meaning that R.union(T).equals(T.union(R)) == true , R and T being instances of Range.
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
	
	private boolean equalsInterval(Interval<T> other){
		return (this.isEmpty() && other.isEmpty() ) || // both are empty or the limits are equal
		( comparator.compare(start, other.start)==0 && comparator.compare(end, other.end)==0);
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
	


	@Override
	public Iterator<T> iterator() {
		return new RangeIterator(comparator,incrementor);
	}

	/**
	 * Collects all the values in the {@link Range} into an {@link EnhancedList}.
	 * @return a list with the values in the {@link Range}
	 */
	private EnhancedList<T> toList(){
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
		for (Iterator<T> it = this.iterator();it.hasNext();size++){
			it.next();
		}
		return size;
	}

    class RangeIterator implements Iterator<T> {

		T current;
		Comparator<? super T> comparator;
		Incrementor<? super T> incrementor;

		public RangeIterator(Comparator<? super T> comparator , Incrementor<? super T> incrementor){
			this.comparator = comparator;
			this.incrementor = incrementor;
			
			if (excludeStart) {
				this.current = (T) incrementor.increment(Range.this.start);
			} else {
				this.current = Range.this.start;
			}
			
		}

		@Override
		public boolean hasNext() {
			return contains(current);
			//return (isReversed ?  comparator.compare(current, Range.this.end) >= 0 : comparator.compare(current, Range.this.end) <= 0);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			T toReturn = current;
			current = (T) incrementor.increment(current);
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
	public boolean any(Predicate<T> predicate) {
		return this.toList().any(predicate);
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
	public boolean every(Predicate<T> predicate) {
		return this.toList().every(predicate);
	}

	@Override
	public T find(Predicate<T> predicate) {
		return this.toList().find(predicate);
	}

	@Override
	public EnhancedCollection<T> findAll(Predicate<T> predicate) {
		return this.toList().findAll(predicate);
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



	/**
	 * {@inheritDoc}
	 */
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
	
	/**
	 * @return Returns the end.
	 */
	public T end() {
		return end;
	}

	/**
	 * @return Returns the start.
	 */
	public T start() {
		return start;
	}


	/**
	 * Determines if this interval intersects another
	 * @param other the candidate to intersection
	 * @return <code>true</code> if <code>this</code> intersects <code>other</code>
	 */
	public boolean intersects (Range<T> other){

		return !this.isEmpty() && !other.isEmpty() &&
		(this.contains(other.start, true, true) || 
				this.contains(other.end, true, true) || 
				other.contains(this.start, true, true) || 
				other.contains(this.end, true, true));


	}

	/**
	 * Determines the maximum of two values using the 
	 * comparator
	 * @param a
	 * @param b
	 * @return the maximum between a and b
	 */
	protected T  max(T a , T b){
		if (comparator.compare(a,b)>=0){
			return a;
		}
		return b;
	}

	/**
	 * Determines the minimum of two values using the 
	 * comparator
	 * @param a
	 * @param b
	 * @return the minimum between a and b
	 */
	protected T  min(T a , T b){
		if (comparator.compare(a,b)<=0){
			return a;
		}
		return b;
	}

	public String toString(){
		if (this.isEmpty()){
			return "[]";
		}
		return "[" + start.toString() + " ; " +  end.toString() + "]";
	}








}

