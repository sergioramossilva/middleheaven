package org.middleheaven.util.measure.structure;


public class EditableMatrix<F extends Field<F>> extends DenseMatrix<F> {

	EditableMatrix(Matrix<F> other) {
		super(other);
	}
	
	EditableMatrix(int rows, int columns, F value) {
		super(rows,columns, value);
	}
	
	EditableMatrix(Matrix<F> other, int[] r, int j0, int j1) {
	     super(r.length,j1-j0+1, null);
	      try {
	         for (int i = 0; i < r.length; i++) {
	            for (int j = j0; j <= j1; j++) {
	               this.set(i, j-j0 , other.get(r[i] , j));
	            }
	         }
	      } catch(ArrayIndexOutOfBoundsException e) {
	         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
	      }
	}

	public EditableMatrix<F> set(int r, int c, F value){
		rows.get(r).elements.set(c, value);
		return this;
	}
	

}
