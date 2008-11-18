package org.middleheaven.math.structure;


public class EditableVector<F extends Field<F>> extends DenseVector<F> {

	EditableVector(Vector<F> other) {
		super(other);
		
	}

	public EditableVector<F> set(int index, F value ){
		this.elements.set(index, value);
		return this;
	}


}
