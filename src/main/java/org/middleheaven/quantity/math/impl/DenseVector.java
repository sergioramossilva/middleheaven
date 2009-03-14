package org.middleheaven.quantity.math.impl;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.quantity.math.Conjugatable;
import org.middleheaven.quantity.math.Vector;
import org.middleheaven.quantity.math.structure.Field;

/**
 * Dense implementation of Vector
 * 
 *
 * @param <F>
 */
class DenseVector<F extends Field<F>> extends Vector<F> implements Conjugatable<DenseVector<F>>{

	
	List<F> elements;
	
	DenseVector(List<F> elements){
		this.elements = new ArrayList<F>(elements);
	}

	DenseVector(int dimention, F value){
		this.elements = new ArrayList<F>(dimention);
		if ( value!=null){
			for (int i=0; i< dimention; i++){
				elements.add(value);
			}
		}
	}

	DenseVector(Vector<F> other) {
		int size = other.size();
		this.elements = new ArrayList<F>(size);
		for (int i=0;  i< size; i++){
			elements.add(other.get(i));
		}
	}
	
	private DenseVector(){
		
	}
	private DenseVector<F> fromPrivateList(List<F> elements){
		DenseVector<F> m = new DenseVector<F>();
		m.elements = elements;
		return m;
	}

	@Override
	public F get(int index) {
		return this.elements.get(index);
	}


	@Override
	public int getDimention() {
		return this.elements.size();
	}



	DenseVector<F> remove(int index) {
		List<F> nElements = new ArrayList<F>(elements);
		nElements.remove(index);
		return fromPrivateList(nElements);
	}

	@SuppressWarnings("unchecked")
	public boolean equals (Object other){
		return other instanceof Vector && equals((Vector<F>)other);
	}

	public boolean equals(Vector<F> other){
		for (int i=0; i < this.getDimention(); i++){
			if (!this.get(i).equals(other.get(i))){
				return false;
			}
		}
		return true;
	}

	public String toString(){
		return elements.toString();
	}

	@Override
	public Vector<F> zero() {
		F zero = this.get(0).zero();
		List<F> nelements = new ArrayList<F>(this.getDimention());
		for (int i=0;i<this.elements.size();i++){
			nelements.add(zero);
		}
		return fromPrivateList(nelements);
	}

	@Override
	public F[] toArray(F[] elementsArray) {
		elements.toArray(elementsArray);
		return elementsArray;
	}
	
	protected DenseVector<F> duplicate(){
		return new DenseVector<F>(this);
	}
	
	public DenseVector<F> conjugate() {
		F value = this.get(0);
		if (value instanceof Conjugatable){
			// conjugate all elements
			List<F> elements = new ArrayList<F>(this.elements.size());
			
			for (F f : this.elements){
				elements.add(((Conjugatable<F>)f).conjugate());
			}
			
			return fromPrivateList(elements);
		} else {
			return duplicate();
		}
	}


}
