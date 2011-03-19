package org.middleheaven.core.reflection.metaclass;


public class ReflectionBean implements MetaBean {

	
	private Object bean;
	private MetaClass metaClass;

	public ReflectionBean(Object bean){
		this(bean, new ReflectionMetaClass(bean.getClass()));
	}
	
	protected ReflectionBean(Object bean, MetaClass metaClass){
		this.bean = bean;
		this.metaClass = metaClass;
	}
	
	
	@Override
	public MetaClass getMetaClass() {
		return metaClass;
	}

	@Override
	public Object get(String name) {
		return metaClass.getPropertyAcessor(name).getValue(bean);
	}

	@Override
	public void set(String name, Object value) {
		metaClass.getPropertyAcessor(name).setValue(bean, value);
	}

}
