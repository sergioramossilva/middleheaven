package org.middleheaven.storage;

import java.lang.reflect.Method;

import javax.el.PropertyNotFoundException;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.MethodIntrospector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.identity.Identity;

public class StorableProxyHandler implements ProxyHandler  {

	
	private Identity identity;
	private StorableState state = StorableState.FILLED;
	Class<?> originalType;
	
    StorableProxyHandler(Class<?> original){
		this.originalType = original;
	}
	
	@Override
	public Object invoke(Object self, Object[] args, MethodDelegator delegator) throws Throwable {
		String methodName = delegator.getName();
		if (!delegator.hasSuper()){
			if (methodName.equals("getFieldValue")){
				
				return this.getFieldValue( self, (StorableFieldModel)args[0]);
				
				
			} else if (methodName.equals("setFieldValue")){

				setFieldValue(self, (StorableFieldModel)args[0],args[1]);
						
				return null;
			} else if (methodName.equals("addFieldElement")){
				
				addFieldElement(self, (StorableFieldModel)args[0],args[1]);
				
				return null;
				
			} else if (methodName.equals("removeFieldElement")){

				removeFieldElement(self, (StorableFieldModel)args[0],args[1]);
						
				return null;
			}else {
				Method method = Introspector.of(originalType).inspect().methods().named(methodName)
								.withParametersType(delegator.getInvoked()
										.getParameterTypes()).retrive();
				
				if (method == null){
					method = Introspector.of(StorableProxyHandler.class).inspect().methods().named(methodName)
					.withParametersType(delegator.getInvoked()
							.getParameterTypes()).retrive();
					
					if (method == null){
						throw new NoSuchMethodException(methodName);
					} else {
						return method.invoke(this, args);
					}
					
				} else {
					if (methodName.startsWith("set")){
						state = state.edit();
					}
					return delegator.invokeSuper(self, args);  // execute the original method.

				}
				
			}
		} else {
			if (methodName.startsWith("set")){
				state = state.edit();
			}
			return delegator.invokeSuper(self, args);  // execute the original method.
		}
	}

	private void removeFieldElement(Object self,StorableFieldModel storableFieldModel, Object object) {
		Class<? extends Object> argumentType = object instanceof Storable ? ((Storable)object).getPersistableClass()  : object.getClass();
		
		MethodIntrospector remover = Introspector.of(Introspector.of(this.originalType).inspect().methods()
				.withParametersType(new Class[]{argumentType})
				.notInheritFromObject()
				.match(new BooleanClassifier<Method>(){

					@Override
					public Boolean classify(Method obj) {
						return obj.getName().startsWith("remove");
					}
					
				})
				.retrive());
		
		remover.invoke(null, self, object);
	}

	private void addFieldElement(Object self, StorableFieldModel storableFieldModel, Object object) {

		Class<? extends Object> argumentType = object instanceof Storable ? ((Storable)object).getPersistableClass()  : object.getClass();
		
		MethodIntrospector adder = Introspector.of(Introspector.of(this.originalType).inspect().methods()
				.withParametersType(new Class[]{argumentType})
				.notInheritFromObject()
				.match(new BooleanClassifier<Method>(){

					@Override
					public Boolean classify(Method obj) {
						return obj.getName().startsWith("add");
					}
					
				})
				.retrive());
		
		adder.invoke(null, self, object);
	}

	private void setFieldValue(Object self,StorableFieldModel model, Object value) {
		String name = model.getHardName().getName();
		
		PropertyAccessor property = Introspector.of(this.originalType).inspect().properties()
				.named(name).retrive();
				
		if (property == null){
			property = Introspector.of(StorableProxyHandler.class).inspect().properties()
				.named(name).retrive();
			if (property == null){
				throw new PropertyNotFoundException(name);
			}
			 property.setValue(this,value);
		} else {
			 property.setValue(self,value);
		}
	}
	
	private Object getFieldValue(Object self,StorableFieldModel model) {
		String name = model.getHardName().getName();
		
		PropertyAccessor property = Introspector.of(originalType).inspect().properties()
									.named(name).retrive();
		
		if (property == null){
			property = Introspector.of(StorableProxyHandler.class).inspect().properties()
				.named(name).retrive();
			if (property == null){
				throw new PropertyNotFoundException(name);
			}
			return property.getValue(this);
			
		} else {
			return property.getValue(self);
		}
	}

	public Identity getIdentity() {
		return identity;
	}
	
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
	
	public String getName() {
		return originalType.getSimpleName();
	}

	public Class<?> getPersistableClass() {
		return originalType;
	}


	public StorableState getPersistableState() {
		return state;
	}

	public void setPersistableState(StorableState state) {
		this.state = state;
	}


	

}
