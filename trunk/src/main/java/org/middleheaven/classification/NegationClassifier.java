/*
 * Created on 2006/09/22
 *
 */
package org.middleheaven.classification;

/**
 * Filter that returns the opposite result  of a underlying filter.
 * @author  Sergio M. M. Taborda
 */
public class NegationClassifier<T> implements BooleanClassifier<T> {


	private BooleanClassifier<T> original;

	public NegationClassifier(){};

	public NegationClassifier(BooleanClassifier<T> original){
		this.original = original;
	}

	public void setFilter(BooleanClassifier<T> filter){
		this.original = filter;
	}

	public BooleanClassifier<T> getFilter(){
		return original;
	}

	@Override
	public Boolean classify(T obj) {
		return !original.classify(obj);
	}
		
		
}


