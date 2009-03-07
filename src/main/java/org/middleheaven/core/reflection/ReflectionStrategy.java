package org.middleheaven.core.reflection;

public interface ReflectionStrategy {

	public PropertyAccessor getPropertyAccessor(Class<?> type, String fieldName);
	public Iterable<PropertyAccessor> getPropertyAccessors(Class<?> type) throws ReflectionException;

		
	public <T> T proxy (Class<T> facadeClass , ProxyHandler handler);
	public <I> I proxy (Object delegationTarget , Class<I> proxyInterface);
	public <I> I proxy (Object delegationTarget , Class<I> proxyInterface , final ProxyHandler handler );
}
