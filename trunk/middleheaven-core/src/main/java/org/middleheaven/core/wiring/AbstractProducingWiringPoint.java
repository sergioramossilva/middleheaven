/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Mergable;

/**
 * 
 */
public abstract class AbstractProducingWiringPoint implements ProducingWiringPoint {

	
	private WiringSpecification<?> methodSpecification;
	private WiringSpecification<?>[] paramsSpecifications;
	
	
	protected AbstractProducingWiringPoint (WiringSpecification<?> methodSpecification,WiringSpecification<?>[] paramsSpecifications){
		this.methodSpecification = methodSpecification;
		this.paramsSpecifications = paramsSpecifications;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public WiringSpecification<?> getSpecification() {
		return methodSpecification;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public WiringSpecification<?>[] getParamsSpecifications() {
		return paramsSpecifications;
	}
	



}
