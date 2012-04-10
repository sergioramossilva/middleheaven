/**
 * 
 */
package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @param <T>
 */
public interface BindingBuilder<T> {

	public abstract QualificationBuilder<T> named(String name);

	public abstract QualificationBuilder<T> profiled(String profile);

	public abstract QualificationBuilder<T> lazy();

	public abstract QualificationBuilder<T> in(Class<? extends Annotation> scope);

	public abstract QualificationBuilder<T> inSharedScope();

	public abstract QualificationBuilder<T> inDefaultScope();
	
	/**
	 * @param params
	 */
	public abstract QualificationBuilder<T> withParams(
			Map<String, Object> params);

	/**
	 * @param params
	 */
	public abstract QualificationBuilder<T> withParam(String key, Object value);

}