package org.middleheaven.util.criteria.text;

import org.middleheaven.text.indexing.IndexableDocument;


public interface TextFieldConstraint<D extends IndexableDocument, B extends AbstractTextCriteriaBuilder<D,B>> {


	/**
	 * The field value equals {@code value}. . 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B eq(Object value);  
	
	/**
	 * 
	 * @return denies the constraint selected next. 
	 */
	public TextFieldConstraint<D, B> not();
	
	
	/**
	 * The field value start with {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B startsWith(CharSequence text);
	
	/**
	 * The field value ends with {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B endsWith(CharSequence text);
	
	/**
	 * The field value contains {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B contains(CharSequence text);
	
	/**
	 * The field value contains {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B near(CharSequence text);
	
	/**
	 * The field value is equal or greater than {@code min} and less or equal than {@code max}.
	 * @param <V>
	 * @param min minimum value
	 * @param max maximum value
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public <V extends Comparable<? super V>> B bewteen(V min, V max);  


	
}
