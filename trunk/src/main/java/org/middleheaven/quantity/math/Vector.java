package org.middleheaven.quantity.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.MathStructuresFactory;
import org.middleheaven.quantity.math.structure.VectorSpace;

public abstract class Vector<F extends Field<F>> implements VectorSpace<Vector<F>,F>{
	public static Vector<Real> vector(java.lang.Number ... elements){
		
		Real[] relements = new Real[elements.length];
		for (int i =0;i < elements.length;i++){
			relements[i] = Real.valueOf(elements[i]);
		}

		return  vector(Arrays.asList(relements));
	}
	
	public static <T extends Field<T>> Vector<T> vector(T ... elements){
		return vector(Arrays.asList(elements));
	}
	
	public static<T extends Field<T>> Vector<T> vector(List<T> elements){
		return MathStructuresFactory.getFactory().vectorize(elements);
	}
	
	public static<T extends Field<T>> Vector<T> vector(int size, T element){
		List<T> elements = new ArrayList<T>(size);
		for (int i=0;  i< size; i++){
			elements.add(element);
		}
		return vector(elements);
	}
	
	public static <T extends Field<T>> Vector<T> vector(Vector<T> other){
		return MathStructuresFactory.getFactory().vectorize(other);
	}

	public static <T extends Field<T>> Vector<T> replicate(int dimension, T value){
		return MathStructuresFactory.getFactory().vectorize(dimension, value);
	}
	
	public abstract F get(int index);
	
	protected abstract int getDimention();
	
	public int size(){
		return this.getDimention();
	}
	
	@SuppressWarnings("unchecked")
	public Vector<F> cross(Vector<F> other){
		if (this.getDimention()!=3 || this.getDimention()!=3){
			throw new ArithmeticException("Cross product it not defined in this space");
		}
		
		return vector(
				this.get(2).times(this.get(3)).minus(this.get(3).times(this.get(2))),
				this.get(3).times(this.get(1)).minus(this.get(1).times(this.get(3))),
				this.get(1).times(this.get(2)).minus(this.get(2).times(this.get(1)))
		);
		
		
	}

	public F times(Vector<F> other){
		if (this.getDimention()!= this.getDimention()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		F result = this.get(0).times(other.get(0));
		for (int i =1; i <this.getDimention(); i++){
			result = result.plus((this.get(i).times(other.get(i))));
		}
		return result;
	}


	
	@Override
	public Vector<F> times(F a) {
		if (this.getDimention()!= this.getDimention()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		List<F> elements = new ArrayList<F>(this.getDimention());
		for (int i =0; i <this.getDimention(); i++){
			elements.add((this.get(i).times(a)));
		}
		return vector(elements);
	}

	@Override
	public final Vector<F> minus(Vector<F> other) {
		return this.plus(other.negate());
	}

	@Override
	public Vector<F> negate() {
		if (this.getDimention()!= this.getDimention()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		List<F> elements = new ArrayList<F>(this.getDimention());
		for (int i =0; i <this.getDimention(); i++){
			elements.add((this.get(i).negate()));
		}
		return vector(elements);
	}

	@Override
	public Vector<F> plus(Vector<F> other) {
		if (this.getDimention()!= this.getDimention()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		List<F> elements = new ArrayList<F>(this.getDimention());
		for (int i =0; i <this.getDimention(); i++){
			elements.add((this.get(i).plus(other.get(i))));
		}
		return vector(elements);
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals (Object other){
		return other instanceof Vector && equals((Vector)other);
	}
	
	public abstract boolean equals (Vector<F> other);
	
	public abstract F[] toArray(F[] elements);
	
}
