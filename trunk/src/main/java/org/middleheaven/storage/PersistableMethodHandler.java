package org.middleheaven.storage;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.ReflectionUtils;

import javassist.util.proxy.MethodHandler;

public class PersistableMethodHandler implements MethodHandler {

	
	private Long key;
	private PersistableState state = PersistableState.FILLED;
	Class<?> original;
	
	PersistableMethodHandler(Class<?> original){
		this.original = original;
	}
	
	@Override
	public Object invoke(Object self, Method invoked, Method original,Object[] args) throws Throwable {
		if (original == null){
			return this.getClass().getMethod(invoked.getName(), ReflectionUtils.typesOf(args)).invoke(this, args);
		} else {
			return original.invoke(self, args);  // execute the original method.
		}
	}
	

	public Long getKey() {
		return key;
	}

	public String getName() {
		return original.getSimpleName();
	}

	public Class<?> getPersistableClass() {
		return original;
	}


	public PersistableState getPersistableState() {
		return state;
	}

	public void setKey(Long key) {
		this.key = key;
	}


	public void setPersistableState(PersistableState state) {
		this.state = state;
	}

	

}
