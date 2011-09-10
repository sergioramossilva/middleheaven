package org.middleheaven.core.reflection.metaclass;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.PropertyNotFoundException;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.util.collections.Enumerable;

/**
 * Wraps a {@link java.lang.Class} in a {@link MetaClass}.
 */
public class ReflectionMetaClass implements MetaClass{

	
	private Class<?> type;

	/**
	 * 
	 * @param type the reflect class.
	 */
	public ReflectionMetaClass(Class<?> type){
		this.type = type;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public PropertyAccessor getPropertyAcessor(String name) {
		PropertyAccessor pa = Introspector.of(type).inspect().properties().named(name).retrive();
		
		if (pa == null){
			throw new PropertyNotFoundException("Property " + name + " not found in " + this.getName());
		}
		return pa;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<PropertyAccessor> getProperties() {
		return Introspector.of(type).inspect().properties().retriveAll();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public MetaBean newInstance() {
		return new ReflectionBean(Introspector.of(type).newInstance(), this);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getSimpleName() {
		return this.getSimpleName();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return type.getName();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsProperty(String name) {
		return  Introspector.of(type).inspect().properties().named(name).retrive() != null;
		
	}

}
