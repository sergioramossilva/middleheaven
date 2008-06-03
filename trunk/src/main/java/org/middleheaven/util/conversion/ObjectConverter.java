package org.middleheaven.util.conversion;

/**
 * 
 * 
 *
 */
public interface ObjectConverter<O> {

	 public <T> T convert(O value, Class<T> targetClass);
}
