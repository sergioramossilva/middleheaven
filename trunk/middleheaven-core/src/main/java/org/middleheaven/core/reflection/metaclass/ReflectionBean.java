package org.middleheaven.core.reflection.metaclass;

/**
 * A {@link MetaBean} implemented by reflection of an Object.
 */
public class ReflectionBean implements MetaBean {

	
	private Object bean;
	private MetaClass metaClass;

	/**
	 * 
	 * Constructor.
	 * @param bean the original object.
	 */
	public ReflectionBean(Object bean){
		this(bean, new ReflectionMetaClass(bean.getClass()));
	}
	
	/**
	 * 
	 * Constructor.
	 * @param bean the original object
	 * @param metaClass the used {@link MetaClass}.
	 */
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