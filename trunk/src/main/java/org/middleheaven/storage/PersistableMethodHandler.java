package org.middleheaven.storage;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;

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
				String name = model.getHardName().getName();
				
				return Introspector.of(self.getClass()).inspect().properties()
							.named(name).retrive().getValue(self);
				
			} else if (methodName.equals("setFieldValue")){
				StorableFieldModel model = (StorableFieldModel)args[0];
				String name = model.getHardName().getName();
				
				Introspector.of(this.originalType).inspect().properties()
						.named(name).retrive().setValue(self,args[1]);
				return null;
			} else {
				return Introspector.of(this.getClass()).inspect().methods().named(methodName).withParametersType(delegator.getInvoked().getParameterTypes())
				.retrive().invoke(this, args);
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
