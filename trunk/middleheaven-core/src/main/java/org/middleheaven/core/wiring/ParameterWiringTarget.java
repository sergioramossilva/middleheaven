/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
class ParameterWiringTarget implements WiringTarget {

	
	private Object instance;
	private Class<?> type;
	private Class<?> declaringType;

	public ParameterWiringTarget (){
		
	}
	/**
	 * Constructor.
	 * @param class1
	 * @param declaringClass
	 * @param object
	 */
	public ParameterWiringTarget(Class<?> type, Class<?> declaringType,Object instance ) {
		this.instance = instance;
		this.type = type;
		this.declaringType = declaringType;
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getDeclaringType() {
		return declaringType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getInstance() {
		return instance;
	}

}
