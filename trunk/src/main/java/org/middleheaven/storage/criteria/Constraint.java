package org.middleheaven.storage.criteria;

import java.util.Collection;

import org.middleheaven.util.collections.Interval;

/**
 * Extention for the CriteriaBuilder to support creation of fields 
 * constrains a fluente interface. 
 *
 * @param <T>
 */
public interface Constraint<T> {

	/**
	 * The attribute value equals {@code value}. . 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public CriteriaBuilder<T> eq(Object value);  
	/**
	 * The attribute value is less than {@code value}. . 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public CriteriaBuilder<T> lt(Object value);  
	
	/**
	 * The attribute value is greater than {@code value}. . 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public CriteriaBuilder<T> gt(Object value);
	/**
	 * The attribute value is less than or equal to {@code value}. 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public CriteriaBuilder<T> le(Object value);  
	/**
	 * The attribute value is greater than or equal to {@code value}. 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public CriteriaBuilder<T> ge(Object value);
	/**
	 * The attribute value is {@code null} 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public CriteriaBuilder<T> isNull();
	/**
	 * The attribute value start with {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public CriteriaBuilder<T> startsWith(CharSequence text);
	/**
	 * The attribute value ends with {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public CriteriaBuilder<T> endsWith(CharSequence text);
	/**
	 * The attribute value contains {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public CriteriaBuilder<T> contains(CharSequence text);
	
	/**
	 * The attribute value is contained in the {@code values} collection.
	 * @param values value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public <V> CriteriaBuilder<T> in(Collection<V> values);
	
	/**
	 * The attribute value is contained in one of the given {@code values}.
	 * @param values value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public <V> CriteriaBuilder<T> in(V ... values);
	/**
	 * The attribute value is contained in the given {@code interval}.
	 * @param interval interval where the attribute's value must be contained
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public <V extends Comparable<? super V>> CriteriaBuilder<T> in(Interval<V> interval);
	/**
	 * The attribute value is equal or greater than {@code min} and less or equal than {@code max}.
	 * @param <V>
	 * @param min minimum value
	 * @param max maximum value
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public <V extends Comparable<? super V>> CriteriaBuilder<T> bewteen(V min, V max);  

	/**
	 * 
	 * @return denies the constraint selected next. 
	 */
	public Constraint<T> not();
	
	
}
