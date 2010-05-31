package org.middleheaven.storage;

import java.lang.reflect.Method;

import javax.el.PropertyNotFoundException;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.MethodIntrospector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.identity.Identity;

public class StorableProxyHandler implements ProxyHandler  {


	private Identity identity;
	private StorableState state = StorableState.FILLED;
	private Class<?> originalType;
	private EntityModel model;

	StorableProxyHandler(Class<?> original, EntityModel model){
		this.originalType = original;
		this.model = model;

	}

	@Override
	public Object invoke(Object self, Object[] args, MethodDelegator delegator) throws Throwable {
		String methodName = delegator.getName();
		if (!delegator.hasSuper()){
			if (methodName.equals("getEntityModel")){
				return model;
			} else if (methodName.equals("getFieldValue")){

				return this.getFieldValue( self, (EntityFieldModel)args[0]);

			} else if (methodName.equals("setFieldValue")){

				setFieldValue(self, (EntityFieldModel)args[0],args[1]);

				return null;
			} else if (methodName.equals("addFieldElement")){

				addFieldElement(self, (EntityFieldModel)args[0],args[1]);

				return null;

			} else if (methodName.equals("removeFieldElement")){

				removeFieldElement(self, (EntityFieldModel)args[0],args[1]);

				return null;
			} else {
				Method method = Introspector.of(originalType).inspect().methods().named(methodName)
				.withParametersType(delegator.getInvoked()
						.getParameterTypes()).retrive();

				if (method == null){

					if (methodName.equals("setIdentity")){
						if (this.model.identityFieldModel()!=null){
							Object id = TypeCoercing.coerce(args[0], this.model.identityFieldModel().getValueType());
							setFieldValue(self, this.model.identityFieldModel(),id);
							return null;
						}
					} else if (methodName.equals("getIdentity")){
						if (this.model.identityFieldModel()!=null){
							Object id = getFieldValue(self, this.model.identityFieldModel());
							return TypeCoercing.coerce(id, delegator.getInvoked().getReturnType());
						}
					}

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

	private void removeFieldElement(Object self,EntityFieldModel fieldModel, Object object) {
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

	private void addFieldElement(Object self, EntityFieldModel fieldModel, Object object) {

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

	private void setFieldValue(Object self,EntityFieldModel model, Object value) {
		String name = model.getLogicName().getName();

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

	private Object getFieldValue(Object self,EntityFieldModel model) {
		String name = model.getLogicName().getName();

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


	public String toString(){
		return this.originalType.getName()  + "#" + identity;
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


	public StorableState getStorableState() {
		return state;
	}

	public void setStorableState(StorableState state) {
		this.state = state;
	}


}
