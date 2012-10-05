package org.middleheaven.quantity.math;

import java.util.Collection;
import java.util.Iterator;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.Predicate;
import org.middleheaven.util.collections.IndexBasedIterator;
import org.middleheaven.util.collections.IterableWalkable;
import org.middleheaven.util.collections.Walkable;
import org.middleheaven.util.collections.Walker;

public abstract class AbstractVector<F extends Field<F>> implements Vector<F> , Conjugatable<Vector<F>> , Iterable<F>{
	
	
	private VectorSpaceProvider provider;

	protected AbstractVector (VectorSpaceProvider provider){
		this.provider = provider;
	}
	
	
	@SuppressWarnings("unchecked")
	public Vector<F> cross(Vector<F> other){
		if (this.size()!=3 || this.size()!=3){
			throw new ArithmeticException("Cross product it not defined in this space");
		}
		
		return provider.vector(
				this.get(2).times(this.get(3)).minus(this.get(3).times(this.get(2))),
				this.get(3).times(this.get(1)).minus(this.get(1).times(this.get(3))),
				this.get(1).times(this.get(2)).minus(this.get(2).times(this.get(1)))
		);
		
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector<F> multiply(final Vector<F> other) {
		return new LazyDelegationVector<F>(this, this.provider, new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).times(other.get(i));
			}

		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final F times(Vector<F> other){
		if (this.size()!= this.size()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		
		F result = this.get(0).times(other.get(0));
		for (int i =1; i <this.size(); i++){
			result = result.plus((this.get(i).times(other.get(i))));
		}
		return result;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public final F[] toArray(F[] elements) {
		for (int i = 0; i < elements.length ; i++){
			elements[i] = this.get(i);
		}
		
		return elements;
	}
	

	@Override
	public Vector<F> times(final F a) {

		return new LazyDelegationVector<F>(this, this.provider, new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).times(a);
			}

		});
		
	}

	@Override
	public final Vector<F> minus(final Vector<F> other) {
		if (this.size()!= this.size()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		
		return new LazyDelegationVector<F>(this, this.provider, new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).minus(other.get(i));
			}

		});
	}

	@Override
	public Vector<F> negate() {
		
		return new LazyDelegationVector<F>(this, this.provider, new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).negate();
			}

		});

	}

	@Override
	public Vector<F> plus(final Vector<F> other) {
		if (this.size()!= this.size()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		
		return new LazyDelegationVector<F>(this, this.provider, new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).plus(other.get(i));
			}

		});
		
	}


	public final Vector<F> conjugate() {
		
		return new LazyDelegationVector<F>(this, this.provider, new ValueResolver<F>(){

			@SuppressWarnings("unchecked")
			@Override
			public F resolve(int i, Vector<F> original) {
				F value = original.get(i);
				if (value instanceof Conjugatable){
					return ((Conjugatable<F>) value).conjugate();
				} else {
					return value;
				}
			}

		});

	}
	
	@Override
	public final Vector<F> zero() {
		F zero = this.get(0).zero();
		
		return this.provider.vector(this.size(), zero);
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean equals (Object other){
		return other instanceof Vector && equalsOther((Vector<F>)other);
	}

	protected boolean equalsOther(Vector<F> other){
		for (int i=0; i < this.size(); i++){
			if (!this.get(i).equals(other.get(i))){
				return false;
			}
		}
		return true;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder("[");
		
		for (int i=0; i < this.size(); i++){
			builder.append(this.get(i)).append( ",");
		}
		
		return builder.append("]").toString();
	}




	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<F> iterator() {
		return new IndexBasedIterator<F>(){

			@Override
			protected int getSize() {
				return size();
			}

			@Override
			protected F getObject(int index) {
				return get(index);
			}
			
		};
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Walkable<C> map(Classifier<C, F> classifier) {
		return new IterableWalkable<F>(this).map(classifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Walkable<F> filter(Predicate<F> predicate) {
		return new IterableWalkable<F>(this).filter(predicate);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Walker<F> walker) {
		
		if (this.size() != 0){
			for (int i=0; i < this.size(); i++){
				walker.doWith(this.get(i));
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<F>> L into(L collection) {
		for (int i=0; i < this.size(); i++){
			collection.add(this.get(i));
		}
		return collection;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <N extends Field<N>> Vector<N> apply(final UnivariateFunction <F, N> function) {
		
		return new LazyProxyVector<N>(this.provider, this.size()) {

			@Override
			public N lazyGet(int index) {
				return function.apply(AbstractVector.this.get(index));
			}
			
		};
	}

}
