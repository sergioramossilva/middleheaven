/**
 * 
 */
package org.middleheaven.core.wiring;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Mergable;

/**
 * Implements a {@link ProducingWiringPoint} using a static factory {@link Method}.
 */
public class FactoryMethodWiringPoint extends AbstractProducingWiringPoint {

	private Method method;

	/**
	 * Constructor.
	 * @param method the method that will produce the object.
	 * @param specification the method wiring specification
	 * @param paramsSpecifications the method params wiring specification
	 */
	protected FactoryMethodWiringPoint(
			Method method,
			WiringSpecification<?> specification,
			WiringSpecification<?>[] paramsSpecifications) {
		super(specification, paramsSpecifications);
		
		this.method = method;
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
		

		return new FactoryMethodWiringPoint (this.method, thisSpecification, (WiringSpecification<?>[]) c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T produceWith(EditableBinder binder) {
		try {
			method.setAccessible(true);

			WiringSpecification<?>[] paramsSpecifications = this.getParamsSpecifications();
			int length = paramsSpecifications.length;

			Object[] params = new Object[length];
			for (int i=0;i< length; i++){
				params[i] = binder.getInstance(paramsSpecifications[i]);
				if (params[i]==null){ // constructor params are always mandatory even if configured otherwise 
					throw new BindingException("Value to bind with parameter of index " + i +  
							" in method " + method.getDeclaringClass().getName() +"." + 
							method.getName()+ " was not found ");
				}
			}


			@SuppressWarnings("unchecked") T newInstance = (T) method.invoke(null, params); // is an static method
			return newInstance;
		} catch (BindingException e){
			throw e;
		} catch (Exception e) {
			throw ReflectionException.manage(e,method.getDeclaringClass());
		}
	}

}
