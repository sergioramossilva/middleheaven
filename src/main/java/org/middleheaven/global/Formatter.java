/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.global;

public interface Formatter<T> {

    public String format(T object);
    public T parse(String stringValue);
    
}
