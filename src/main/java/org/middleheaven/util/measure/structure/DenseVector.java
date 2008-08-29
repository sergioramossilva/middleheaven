package org.middleheaven.util.measure.structure;

import java.util.ArrayList;
import java.util.List;

public class DenseVector<F extends Field<F>> extends Vector<F> {

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
		this.elements = new ArrayList<F>(other.getDimention());
		for (int i=0;  i< other.getDimention(); i++){
			elements.add(other.get(i));
		}
	}

	@Override
	F get(int index) {
		return this.elements==null ? null : this.elements.get(index);
	}


	@Override
	int getDimention() {
		return this.elements.size();
	}



	DenseVector<F> remove(int index) {
		List<F> nElements = new ArrayList<F>(elements);
		nElements.remove(index);
		return new DenseVector<F>(nElements);
	}

	public boolean equals(Object other){
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
		return new DenseVector<F>(nelements);
	}


}
