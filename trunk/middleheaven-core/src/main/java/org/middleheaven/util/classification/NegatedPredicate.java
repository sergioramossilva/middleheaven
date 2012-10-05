/*
 * Created on 2006/09/22
 *
 */
package org.middleheaven.util.classification;


/**
 * Filter that returns the opposite result  of a underlying filter.
 *
 */
public class NegatedPredicate<T> implements Predicate<T> {


	private Classifier<Boolean,T>  original;

	public NegatedPredicate(){};

	public NegatedPredicate(Classifier<Boolean,T>  original){
		this.original = original;
	}

	public void setFilter(Predicate<T> filter){
		this.original = filter;
	}

	public Classifier<Boolean,T>  getFilter(){
		return original;
	}

	@Override
	public Boolean classify(T obj) {
		return !original.classify(obj);
	}
		
		
}


