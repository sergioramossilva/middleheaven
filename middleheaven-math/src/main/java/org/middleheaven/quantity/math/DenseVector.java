package org.middleheaven.quantity.math;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.Predicate;
import org.middleheaven.util.collections.Walkable;
import org.middleheaven.util.collections.Walker;

/**
 * Dense implementation of Vector
 * 
 *
 * @param <F>
 */
public class DenseVector<F extends Field<F>> extends AbstractVector<F> {


	Object[] elements;


	public DenseVector(List<F> list){
		this(list.size());

		this.elements = list.toArray(this.elements);
	}

	public DenseVector(int dimension){
		super(new DenseVectorSpaceProvider());
		this.elements = new Object[dimension];

	}

	public DenseVector(int dimension, F value){
		this(dimension);

		if ( value!=null){
			Arrays.fill(this.elements, value);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DenseVector(Vector<F> other) {
		this(other.size());
		
		for (int i=0;  i< other.size(); i++){
			elements[i] =other.get(i);
		}



	}

	public DenseVector<F> set(int index, F value){
		this.elements[index] =  value;
		return this;
	}


	@Override
	public final F get(int index) {
		return (F)this.elements[index];
	}

	@Override
	public final int size() {
		return this.elements.length;
	}









}
