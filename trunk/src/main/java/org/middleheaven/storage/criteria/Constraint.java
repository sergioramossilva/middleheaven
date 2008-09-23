package org.middleheaven.storage.criteria;

import java.util.Collection;

import org.middleheaven.util.Interval;


public interface Constraint<T> {

	public CriteriaBuilder<T> eq(Object value);  
	public CriteriaBuilder<T> lt(Object value);  
	public CriteriaBuilder<T> gt(Object value);  
	public CriteriaBuilder<T> le(Object value);  
	public CriteriaBuilder<T> ge(Object value);
	public CriteriaBuilder<T> isNull();
	public CriteriaBuilder<T> startsWith(CharSequence text);
	public CriteriaBuilder<T> endsWith(CharSequence text);
	public CriteriaBuilder<T> contains(CharSequence text);
	
	
	public <V> CriteriaBuilder<T> in(Collection<V> values);
	public <V> CriteriaBuilder<T> in(V ... values);
	public <V extends Comparable<? super V>> CriteriaBuilder<T> in(Interval<V> interval);
	public <V extends Comparable<? super V>> CriteriaBuilder<T> bewteen(V min, V max);  

	public Constraint<T> not();
	
	
}
