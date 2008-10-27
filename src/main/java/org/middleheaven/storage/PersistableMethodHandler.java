package org.middleheaven.storage;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.ReflectionUtils;

import javassist.util.proxy.MethodHandler;

public class PersistableMethodHandler implements MethodHandler  {

	
	private Long key;
	private PersistableState state = PersistableState.FILLED;
	Class<?> originalType;
	
	PersistableMethodHandler(Class<?> original){
		this.originalType = original;
	}
	
	@Override
	public Object invoke(Object self, Method invoked, Method original,Object[] args) throws Throwable {
		if (original == null){
			if (invoked.getName().equals("getFieldValue")){
				StorableFieldModel model = (StorableFieldModel)args[0];
				String name = model.getHardName().getColumnName();
				
				return ReflectionUtils.getPropertyAccessor(self.getClass(),name).getValue(self);
			} else if (invoked.getName().equals("setFieldValue")){
				StorableFieldModel model = (StorableFieldModel)args[0];
				String name = model.getHardName().getColumnName();
				
				ReflectionUtils.getPropertyAccessor(this.originalType,name).setValue(self,args[1]);
				return null;
			} else {
				return this.getClass().getMethod(invoked.getName(), invoked.getParameterTypes()).invoke(this, args);
			}
		} else {
			if (invoked.getName().startsWith("set")){
				state = state.edit();
			}
			return original.invoke(self, args);  // execute the original method.
		}
	}

	public Long getKey() {
		return key;
	}

	public String getName() {
		return originalType.getSimpleName();
	}

	public Class<?> getPersistableClass() {
		return originalType;
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
