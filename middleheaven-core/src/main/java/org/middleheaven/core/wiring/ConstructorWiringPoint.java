package org.middleheaven.core.wiring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Mergable;

/**
 * Implements a {@link ProducingWiringPoint} using a {@link Constructor}.
 */
public final class ConstructorWiringPoint extends AbstractProducingWiringPoint implements ProducingWiringPoint{

	private Constructor<?> constructor;


	/**
	 * 
	 * Constructor.
	 * @param constructor the constructor to use to produce the object.
	 * @param specification the constructor wiring specification.
	 * @param paramsSpecifications the params wiring specifications.
	 */
	public ConstructorWiringPoint(Constructor<?> constructor, WiringSpecification<?> specification,WiringSpecification<?>[] paramsSpecifications) {
		super(specification , paramsSpecifications);

		if (Modifier.isPrivate(constructor.getModifiers())){
			throw new IllegalArgumentException("Constructor is private");
		}
		this.constructor = constructor;

	}

	/**
	 * Constructs the object.
	 * @param <T> the class of the constructed object.
	 * @param binder the current wiring binder.
	 * @return the constructed object.
	 */
	public <T> T produceWith(EditableBinder binder){
		try {
			constructor.setAccessible(true);

			WiringSpecification<?>[] paramsSpecifications = this.getParamsSpecifications();
			int length = paramsSpecifications.length;

			Object[] params = new Object[length];
			for (int i=0;i< length; i++){
				params[i] = binder.getInstance(paramsSpecifications[i]);
				if (params[i]==null){ // constructor params are always mandatory even if configured otherwise 
					throw new WiringSpecificationNotSatisfiedException(this.constructor.getDeclaringClass(), paramsSpecifications[i]);
				}
			}


			@SuppressWarnings("unchecked") T newInstance = (T) constructor.newInstance(params);
			return newInstance;
		} catch (BindingException e){
			throw e;
		} catch (Exception e) {
			throw ReflectionException.manage(e,constructor.getDeclaringClass());
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProducingWiringPoint merge(ProducingWiringPoint other) {
		if (other ==null){
			return this;
		}

		WiringSpecification thisSpecification = this.getSpecification();
		if (thisSpecification !=null){
			thisSpecification = thisSpecification.merge(other.getSpecification());
		}
		
		Mergable[] a = 	this.getParamsSpecifications();
		Mergable[] b = 	other.getParamsSpecifications();
		
		Mergable[] c  = CollectionUtils.merge(a,b);
		

		return new ConstructorWiringPoint (this.constructor, thisSpecification, (WiringSpecification<?>[]) c);
		
	}

}
