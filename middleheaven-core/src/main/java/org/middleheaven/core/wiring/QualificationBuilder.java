/**
 * 
 */
package org.middleheaven.core.wiring;


/**
 * 
 */
public interface QualificationBuilder<T> extends BindingBuilder<T>{

	public void toInstance(T object);
	
	public void toResolver(Class<Resolver> type);
	
	public void to(Class<? extends T> type);
}
