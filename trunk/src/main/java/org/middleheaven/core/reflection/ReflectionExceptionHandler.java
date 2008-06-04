package org.middleheaven.core.reflection;

import java.lang.reflect.InvocationTargetException;

import org.middleheaven.core.exception.ExceptionHandler;

public class ReflectionExceptionHandler implements ExceptionHandler {

	@Override
	public void handle(Exception exception) throws RuntimeException {
		try{
			throw exception;
		} catch (IllegalAccessException e) {
			throw new IllegalAccesReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccesReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodReflectionException();
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (Exception e){
			throw new ReflectionException(e);
		}
	}

}
