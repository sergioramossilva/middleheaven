package org.middleheaven.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Provides mathematic interval semantics and operations 
 * 
 * @author Sergio M.M. Taborda
 */
public class Range<T> extends Interval<T> implements Iterable<T> {


	private Incrementor<? super T> incrementor;

	public static <V> Range<V> emptyRange(){
		return new Range<V>(new ComparableComparator<V>(), EmptyIncrementor.emptyIncrementor());
	}

	public static <V> Range<V> emptyRange(Comparator<? super V> comparator){
		return new Range<V>(comparator,EmptyIncrementor.emptyIncrementor());
	}

	/**
	 * Compatability method to enable simple use with <code>Integer</code> and other <code>Number</code>
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

		return over(start,end,new ComparableComparator<N>(),new NumberIncrementor(increment));
	}



	
	public static <N extends java.lang.Number> Range<N> over(N start, N end){
		if (start == null || end == null){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		return over(start,end,new ComparableComparator<N>(),new NumberIncrementor(new Long(1)));
	}
	
	/**
	 * Creates a Range from the values of two objects and a <code>Comparator</code> for those objects.
	 * @param <V>
	 * @param start start of the range
	 * @param end end of the range
	 * @param comparator <code>Comparator</code> encapsulating the rules of order for the passed object class
	 * @return a <code>Range</code> from <code>start</code> to <code>end</code>
	 */
	@SuppressWarnings("unchecked")
	public static <N,V extends Incrementable<N>> Range<V> over(V start, V end , Object increment){
		if (start == null || end == null || increment==null){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		Incrementor<V> incrementor;
		if (Incrementable.class.isInstance(start)){
			incrementor = new IncrementableIncrementor<V,N>((N)increment);
		} else {
			throw new IllegalStateException("Cannot define an Incrementor");
		}
		return over(start,end,new ComparableComparator<V>(),incrementor);
	
	}
	
	public static <V> Range<V> over(V start, V end, Incrementor<? super V> incrementor  ){
		if (start == null || end == null || incrementor==null){
			throw new IllegalArgumentException("Argument cannot be null");
		}
		return over(start,end,new ComparableComparator<V>(),incrementor);
	}
	
	/**
	 * Creates a Range from the values of two objects and a <code>Comparator</code> for those objects.
	 * @param <V>
	 * @param start start of the range
	 * @param end end of the range
	 * @param comparator <code>Comparator</code> encapsulating the rules of order for the passed object class
	 * @return a <code>Range</code> from <code>start</code> to <code>end</code>
	 */
	public static <V> Range<V> over(V start, V end, Comparator<? super V> comparator, Incrementor<? super V> incrementor  ){
		if (comparator == null){
			throw new NullPointerException("A comparator is required.");
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
		return other instanceof Range && this.equals((Range)other);
	}

	/**
	 * Ranges are equal if their starts are equal and their ends are equals
	 */
	public boolean equals(Range<T> other){
		return (this.isEmpty() && other.isEmpty() ) || // both are empty or the limits are equal
		( comparator.compare(start, other.start)==0 && comparator.compare(end, other.end)==0);
	}

	@Override
	public Iterator<T> iterator() {
		return new RangeIterator<T>(start,end,comparator,incrementor);
	}

	public List<T> toList(){
		List<T> list = new LinkedList<T>();
		for (T i : this){
			list.add(i);
		}
		return list;
	}

	public int size(){
		int size=0;
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



}

