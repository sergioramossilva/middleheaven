package org.middleheaven.core.reflection;

public interface ReflectionStrategy {

	public PropertyAccessor getPropertyAccessor(Class<?> type, String fieldName);
	public Iterable<PropertyAccessor> getPropertyAccessors(Class<?> type) throws ReflectionException;

		
	public <T> T proxy (Class<T> facadeClass , ProxyHandler handler);
	public <I> I proxy (Object delegationTarget , Class<I> proxyInterface);
	
	/**
	 * Determine the real class type. 
	 * Removes all proxies created by this strategy and determines the real class.
	 * @param type
	 * @return
	 */
	public Class<?> getRealType(Class<?> type);
	
	/**
	 * 
	 * @param <I>
	 * @param delegationTarget
	 * @param handler
	 * @param proxyInterface
	 * @return an object that is instanceof {@code delegationTarget} and {@code proxyInterface}. Also this object implements {@code WrapperProxy} interface
	 */
	public <I> I proxy (Object delegationTarget , final ProxyHandler handler , Class<I> proxyInterface , Class<?> ... adicionalInterfaces);
}
