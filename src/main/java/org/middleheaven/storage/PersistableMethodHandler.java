package org.middleheaven.storage;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.ReflectionUtils;

public class PersistableMethodHandler implements ProxyHandler  {

	
	private Long key;
	private PersistableState state = PersistableState.FILLED;
	Class<?> originalType;
	
	PersistableMethodHandler(Class<?> original){
		this.originalType = original;
	}
	
	@Override
	public Object invoke(Object self, Object[] args, MethodDelegator delegator) throws Throwable {
		String methodName = delegator.getName();
		if (!delegator.hasSuper()){
			if (methodName.equals("getFieldValue")){
				StorableFieldModel model = (StorableFieldModel)args[0];
				String name = model.getHardName().getColumnName();
				
				return ReflectionUtils.getPropertyAccessor(self.getClass(),name).getValue(self);
			} else if (methodName.equals("setFieldValue")){
				StorableFieldModel model = (StorableFieldModel)args[0];
				String name = model.getHardName().getColumnName();
				
				ReflectionUtils.getPropertyAccessor(this.originalType,name).setValue(self,args[1]);
				return null;
			} else {
				return ReflectionUtils.getMethod(this.getClass(), methodName, delegator.getInvoked().getParameterTypes()).invoke(this, args);
			}
		} else {
			if (methodName.startsWith("set")){
				state = state.edit();
			}
			return delegator.invokeSuper(self, args);  // execute the original method.
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
