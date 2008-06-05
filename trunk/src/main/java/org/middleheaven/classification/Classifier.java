package org.middleheaven.classification;

public interface Classifier<C , T > {

	public C classify (T obj); 
}
