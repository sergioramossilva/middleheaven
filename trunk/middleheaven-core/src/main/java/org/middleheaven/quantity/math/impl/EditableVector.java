package org.middleheaven.quantity.math.impl;

import org.middleheaven.quantity.math.Vector;
import org.middleheaven.quantity.math.structure.Field;


class EditableVector<F extends Field<F>> extends DenseVector<F> {

	EditableVector(Vector<F> other) {
		super(other);
		
	}

	public EditableVector<F> set(int index, F value ){
		this.elements.set(index, value);
		return this;
	}
	
	protected DenseVector<F> duplicate(){
		return new EditableVector<F>(this);
	}


}
