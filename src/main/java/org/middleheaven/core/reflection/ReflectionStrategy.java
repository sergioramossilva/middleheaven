package org.middleheaven.core.reflection;

public interface ReflectionStrategy {

	public PropertyAccessor getPropertyAccessor(Class<?> type, String fieldName);
	public Iterable<PropertyAccessor> getPropertyAccessors(Class<?> type) throws ReflectionException;

		
	public <T> T proxy (Class<T> facadeClass , ProxyHandler handler);
	public <I> I proxy (Object delegationTarget , Class<I> proxyInterface);
	
	/**
	 * 
	 * @param <I>
	 * @param delegationTarget
	 * @param handler
	 * @param proxyInterface
	 * @return an object that is intanceof {@code delegationTarget} and {@code proxyInterface}. Also this object implements {@code WrapperProxy} interface
	 */
	public <I> I proxy (Object delegationTarget , final ProxyHandler handler , Class<I> proxyInterface , Class<?> ... adicionalInterfaces);
}
