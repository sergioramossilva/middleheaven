/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * A wiring point that produces (i.e. obtains) an object.
 */
public interface ProducingWiringPoint extends WiringPoint {

	
	public ProducingWiringPoint merge(ProducingWiringPoint other);
	
	public WiringSpecification<?> getSpecification();

	public WiringSpecification<?>[] getParamsSpecifications();

	public <T> T produceWith(EditableBinder binder);
}
