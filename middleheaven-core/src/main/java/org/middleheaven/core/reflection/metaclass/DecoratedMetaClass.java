package org.middleheaven.core.reflection.metaclass;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.PropertyNotFoundException;
import org.middleheaven.util.collections.EnhancedArrayList;
import org.middleheaven.util.collections.Enumerable;

public class DecoratedMetaClass implements MetaClass {
	
	private MetaClass original;
	
	private Map<String, PropertyAccessor> acessors = new HashMap<String, PropertyAccessor>();
	
	public DecoratedMetaClass (MetaClass original){
		this.original = original;	
	
	}
	
	/**
	 * 
	 * @param acessor
	 */
	public void addProperty(PropertyAccessor acessor){
		this.acessors.put(acessor.getName(), acessor);
	}
	
	@Override
	public PropertyAccessor getPropertyAcessor(String name) throws PropertyNotFoundException {
		PropertyAccessor pa = this.acessors.get(name);
		
		if (pa == null){
			return this.acessors.get(name);
		}
		return pa;
	}

	@Override
	public Enumerable<PropertyAccessor> getProperties() {
		EnhancedArrayList<PropertyAccessor> allProperties = new EnhancedArrayList<PropertyAccessor>(original.getProperties());
		
		allProperties.addAll(this.acessors.values());
		
		return allProperties;
	}

	@Override
	public MetaBean newInstance() {
		MetaBean originalBean = original.newInstance();
		
		return new DecoratedReflectionBean(originalBean, original, this);
	}

	@Override
	public String getSimpleName() {
		return original.getSimpleName();
	}

	@Override
	public String getName() {
		return original.getName();
	}

	@Override
	public boolean containsProperty(String name) {
		return this.original.containsProperty(name) || this.acessors.containsKey(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotation) {
		return (A) original.getAnnotation(annotation);
	}

}
