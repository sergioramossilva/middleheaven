/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * {@link Resolver} implementation that always returns <code>null</code>.
 */
@SuppressWarnings("rawtypes")
public class NullResolver<T> implements Resolver<T> {

	private static final NullResolver INSTANCE = new NullResolver();
	
	
	public static <X> NullResolver<X> instance(){
		return INSTANCE;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T resolve(WiringSpecification<T> specification) {
		return null;
	}

}
