/*
 * Created on 5/Ago/2006
 *
 */
package org.middleheaven.core.exception;

public interface ExceptionHandler<T extends Exception, R extends RuntimeException> {

    public void handle(T exception) throws R;
}
