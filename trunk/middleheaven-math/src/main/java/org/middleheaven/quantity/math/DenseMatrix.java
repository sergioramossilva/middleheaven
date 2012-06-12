package org.middleheaven.quantity.math;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.quantity.math.impl.SingleValueVectorDiagonalMatrix;
import org.middleheaven.quantity.math.structure.DefaultMatrixInvertion;
import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.MathStructuresFactory;

public class DenseMatrix<F extends Field<F>> extends Matrix<F> {

	
	public static <T extends Field<T>> Vector<T> vectorize(List<T> elements) {
		return new DenseVector<T>(elements);
	}

	public static <T extends Field<T>> Vector<T> vectorize(Vector<T> other) {
		return new DenseVector<T>(other);
	}

	public static <T extends Field<T>> Vector<T> vectorize(int dimension, T value) {
		return new DenseVector<T>(dimension,value);
	}

	public static <T extends Field<T>> Matrix<T> matrixFrom(List<Vector<T>> rows) {
			return new DenseMatrix<T>(rows); 
	}

	public static <T extends Field<T>> Matrix<T> diagonal(int size, T value) {
		return new SingleValueVectorDiagonalMatrix<T>(size, value);
	}
	
	public static <T extends Field<T>> Matrix<T> augmentWithEntity(Matrix<T> m){

		DenseMatrix<T> e = new DenseMatrix<T>(m.rowsCount(), m.columnsCount()*2, m.get(0, 0).zero());
		for (int r = 0; r < m.rowsCount(); r++) {
			for (int c = 0; c< m.columnsCount(); c++) {
				e.set(r,c,m.get(r, c));
			}
			e.set(r, m.columnsCount()+ r,m.get(0, 0).one() );
		}
		return e;
	}
	
	protected List<DenseVector<F>> rows;
	private int columnsCount;
	private int rowsCount;

	public DenseMatrix(int rowsCount, int columnsCount, F value){
		this.rowsCount = rowsCount;
		this.columnsCount = columnsCount;
		rows = new ArrayList<DenseVector<F>>(rowsCount);

		for (int r = 0; r < this.rowsCount; r++){
			rows.add(new DenseVector<F>(columnsCount, value));
		}
	}

	public DenseMatrix(Matrix<F> other){
		this.rowsCount = other.rowsCount();
		this.columnsCount = other.columnsCount();
		rows = new ArrayList<DenseVector<F>>();

		for (int r = 0; r < this.rowsCount; r++){
			rows.add(new DenseVector<F>(other.getRow(r)));
		}
	}


	public DenseMatrix(Matrix<F> other, int ri, int c0, int rowCount, int columnCount) {
		this(rowCount,columnCount,null);
		try {
			for (int i = 0; i < rowCount; i++) {
				for (int j = 0; j < columnCount; j++) {
					this.set(i, j , other.get(ri+i , c0+j));
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}
	
	public DenseMatrix(Matrix<F> other, int[] r, int j0, int j1) {
		this(r.length,j1-j0+1, null);
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
	
	public DenseMatrix(List<Vector<F>> rows){
		this.rowsCount = rows.size();
		this.columnsCount = rows.get(0).size();
		this.rows = new ArrayList<DenseVector<F>>(rows.size());

		for (Vector<F> vector : rows){
			this.rows.add(new DenseVector<F>(vector));
		}

	}
	
	public DenseMatrix<F> set(int r, int c, F value){
		List<F> row = rows.get(r).elements;
		if (row==null){
			row = new ArrayList<F>(this.columnsCount());
			rows.get(r).elements = row;
		}
		
		row.add(c, value);
		return this;
	}

	private DenseMatrix(int rowsCount, int columnsCount){
		this.rowsCount = rowsCount;
		this.columnsCount = columnsCount;
	}

	private DenseMatrix<F> fromPrivateList(List<DenseVector<F>> rows){
		DenseMatrix<F> m = new DenseMatrix<F>(rows.size(),rows.get(0).size());
		m.rows = rows;
		return m;
	}

	@Override
	public int columnsCount() {
		return columnsCount;
	}

	@Override
	public int rowsCount() {
		return rowsCount;
	}

	@Override
	public F get(int r, int c) {
		return rows.get(r).get(c);
	}



	@Override
	public F cofactor(int r, int c) {
		// the determinant of the matriz by removing this row and columns
		if( Math.pow(-1, r+c)==-1){
			return this.remove(r, c).determinant().negate();
		} else {
			return this.remove(r, c).determinant(); 
		}
	}

	@Override
	public Matrix<F> adjoint() {

		// cofactors matrix
		List<DenseVector<F>> trows = new ArrayList<DenseVector<F>>(this.rowsCount);
		for (int r =0; r < this.rowsCount; r++){
			List<F> elements = new ArrayList<F>();
			for (int c =0; c < this.columnsCount; c++){
				elements.add( cofactor(r,c) );
			}
			trows.add(new DenseVector<F>(elements));
		}

		return fromPrivateList(trows).transpose();
	}

	public Vector<F> getRow(int row){
		int n = this.columnsCount();
		List<F> elements = new ArrayList<F>(this.rowsCount);
		for (int i=0; i<n;i++){
			elements.add(this.get(row,i));
		}
		return Vector.vector(elements);
	}

	public Vector<F> getColumn(int column){
		int n = this.rowsCount();
		List<F> elements = new ArrayList<F>(n);
		for (int i=0; i<n;i++){
			elements.add(this.get(i,column));
		}
		return Vector.vector(elements);
	}

	public Vector<F> getDiagonal(){
		int n = this.rowsCount();
		List<F> elements = new ArrayList<F>(n);
		for (int i=1; i<n;i++){
			elements.add(this.get(i,i));
		}
		return Vector.vector(elements);

	}



	@Override
	public F determinant() {
		return determinant(this);
	}

	private static <F extends Field<F>> F determinant(DenseMatrix<F> mat) {

		if (!mat.isSquare()){
			throw new ArithmeticException();
		}

		if(mat.rowsCount() == 1) {
			return mat.get(0,0);
		}

		if(mat.rowsCount() == 2) { 
			return mat.get(0,0).times(mat.get(1,1)).minus(mat.get(0,1).times(mat.get(1,0))); 
		}

		F zero = mat.get(0,0).zero(); // ZERO
		
		F result = zero;
		DenseVector<F> masterRow =  (DenseVector)mat.getRow(0);

		for(int i = 0; i < masterRow.getDimention(); i++) { 
			DenseMatrix<F> temp = mat.remove(0,i);

			F x = masterRow.get(i);
			if (!x.equals(zero)){
				if( Math.pow(-1, i)==-1){
					result = result.plus(determinant(temp).times(x.negate())); 
				} else {
					result = result.plus(determinant(temp).times(x)); 
				}
			}
		}
		return result; 

	} 


	DenseMatrix<F> remove(int row, int column) {
		List<DenseVector<F>> newRows = new ArrayList<DenseVector<F>>(this.rows.size());
		MathStructuresFactory factory = MathStructuresFactory.getFactory();

		for (DenseVector<F> v : this.rows){
			newRows.add( v.remove(column));
		}

		newRows.remove(row);

		return this.fromPrivateList(newRows);
	}


	@Override
	public Vector<F> times(Vector<F> vector) {
		List<F> elements = new ArrayList<F>();

		for (int c =0; c < this.columnsCount; c++){
			elements.add(vector.times(this.getColumn(c)) );
		}

		return Vector.vector(elements);
	}

	@Override
	public Matrix<F> transpose() {
		List<DenseVector<F>> trows = new ArrayList<DenseVector<F>>(this.rowsCount);

		for (int r =0; r < this.rowsCount; r++){
			List<F> elements = new ArrayList<F>();
			for (int c =0; c < this.columnsCount; c++){
				elements.add(this.get(c,r));
			}
			trows.add(new DenseVector<F>(elements));
		}
		return fromPrivateList(trows);
	}

	@Override
	public Matrix<F> times(F a) {
		List<DenseVector<F>> trows = new ArrayList<DenseVector<F>>(this.rowsCount);
		for (int r =0; r < this.rowsCount; r++){
			List<F> elements = new ArrayList<F>();
			for (int c =0; c < this.columnsCount; c++){
				elements.add(this.get(r,c).times(a));
			}
			trows.add(new DenseVector<F>(elements));
		}
		return fromPrivateList(trows);
	}

	@Override
	public Matrix<F> negate() {
		List<DenseVector<F>> trows = new ArrayList<DenseVector<F>>(this.rowsCount);
		for (int r =0; r < this.rowsCount; r++){
			List<F> elements = new ArrayList<F>();
			for (int c =0; c < this.columnsCount; c++){
				elements.add(this.get(r,c).negate());
			}
			trows.add(new DenseVector<F>(elements));
		}
		return fromPrivateList(trows);
	}

	@Override
	public Matrix<F> plus(Matrix<F> other) {
		List<DenseVector<F>> trows = new ArrayList<DenseVector<F>>(this.rowsCount);
		for (int r =0; r < this.rowsCount; r++){
			List<F> elements = new ArrayList<F>();
			for (int c =0; c < this.columnsCount; c++){
				elements.add(this.get(r,c).plus(other.get(r, c)));
			}
			trows.add(new DenseVector<F>(elements));
		}
		return fromPrivateList(trows);
	}

	@Override
	public Matrix<F> times(Matrix<F> other) {


		if (this.columnsCount != other.rowsCount()){
			throw new ArithmeticException("Illegal matrix dimensions.");
		}

		List<DenseVector<F>> all = new ArrayList<DenseVector<F>>(this.rowsCount);
		for (int r =0; r < this.rowsCount ; r++){
			List<F> elements = new ArrayList<F>();

			for (int c =0; c < this.columnsCount; c++){
				elements.add(this.getRow(r).times(other.getColumn(c)) );
			}
			all.add(new DenseVector<F>(elements));
		}


		return fromPrivateList(all);
	}

	public Matrix<F> duplicate(){
		return new DenseMatrix<F>(this);
	}

	@Override
	public boolean hasInverse() {
		F zero = this.get(0, 0).minus(this.get(0, 0));
		return this.isSquare() && !this.determinant().equals(zero);
	}

	@Override
	public Matrix<F> inverse() {

		return new DefaultMatrixInvertion().invert(this);

	}

	@Override
	public Matrix<F> conjugate() {
		F value = this.get(0, 0);
		if (value instanceof Conjugatable){

			// conjugate all elements
			List<DenseVector<F>> rows = new ArrayList<DenseVector<F>>(this.rowsCount);

			for (DenseVector<F> vector : this.rows){
				rows.add(vector.conjugate());
			}

			return this.fromPrivateList(rows);
		} else {
			return new DenseMatrix<F>(this);
		}
	}














}
