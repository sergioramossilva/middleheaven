/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * {@link Resolver} implementation that always returns <code>null</code>.
 */
public class NullResolver implements Resolver {

	private static final NullResolver INSTANCE = new NullResolver();
	
	
	public static  NullResolver instance(){
		return INSTANCE;
	}
	
	private NullResolver(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {
		return null;
	}

}
