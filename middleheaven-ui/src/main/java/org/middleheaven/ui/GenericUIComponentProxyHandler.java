package org.middleheaven.ui;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.PropertyBagProxyHandler;
import org.middleheaven.core.reflection.inspection.Introspector;

class GenericUIComponentProxyHandler extends PropertyBagProxyHandler {

	private GenericUIComponent original;
	private Class<?> uiClass;

	public GenericUIComponentProxyHandler(GenericUIComponent object,Class<?> uiClass) {
		this.original = object;
		this.uiClass = uiClass;
		this.setProperty("type", uiClass);
		
	}

	@Override
	public Object invoke(Object proxy, Object[] args, MethodDelegator delegator)	throws Throwable {
		
		Method m = Introspector.of(GenericUIComponent.class).inspect().methods()
			.named(delegator.getName())
			.withParametersType(delegator.getInvoked().getParameterTypes())
			.retrive();
		
		if (m!=null){
			return m.invoke(original,args);
		}
		return null;
		
	}

}
