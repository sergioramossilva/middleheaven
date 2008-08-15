
package org.middleheaven.core.exception;

public interface ExceptionHandler<T extends Throwable, R extends Exception> {

    public R handle(T exception);
}
