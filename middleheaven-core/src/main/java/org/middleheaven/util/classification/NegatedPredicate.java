/*
 * Created on 2006/09/22
 *
 */
package org.middleheaven.util.classification;

import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;


/**
 * Filter that returns the opposite result  of a underlying filter.
 *
 */
public class NegatedPredicate<T> implements Predicate<T> {


	private Mapper<Boolean,T>  original;

	public NegatedPredicate(){};

	public NegatedPredicate(Mapper<Boolean,T>  original){
		this.original = original;
	}

	public void setFilter(Predicate<T> filter){
		this.original = filter;
	}

	public Mapper<Boolean,T>  getFilter(){
		return original;
	}

	@Override
	public Boolean apply(T obj) {
		return !original.apply(obj);
	}
		
		
}


