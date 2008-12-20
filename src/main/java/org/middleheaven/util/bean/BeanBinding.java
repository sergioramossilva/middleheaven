package org.middleheaven.util.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.util.classification.BooleanClassifier;

public class BeanBinding implements PropertyChangeListener{

	Object target;
	
	public static BeanBinding bind(Object source, Object target){
		return new BeanBinding(source,target);
	}
	
	public BeanBinding(Object source, Object target){
		List<Method> list = ReflectionUtils.getMethods (source.getClass(), new BooleanClassifier<Method>(){

			@Override
			public Boolean classify(Method obj) {
				return obj.getName().equalsIgnoreCase("addPropertyChangeListener");
			}
			
		});
		if (list.isEmpty()){
			throw new ReflectionException(source + " is not a bean");
		}
		try {
			list.get(0).invoke(source, new Object[]{this});
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(e);
		}
		
		this.target = target;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		PropertyAccessor p = ReflectionUtils.getPropertyAccessor(target.getClass(), event.getPropertyName());
		
		p.setValue(target, event.getNewValue());
	}
}
