package org.middleheaven.ui.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.util.classification.Predicate;
import org.middleheaven.util.collections.EnhancedCollection;

public class BeanBinding implements PropertyChangeListener{

	Object target;
	
	public static BeanBinding bind(Object source, Object target){
		return new BeanBinding(source,target);
	}
	
	public BeanBinding(Object source, Object target){
		
		EnhancedCollection<Method> list = Introspector.of(source.getClass()).inspect().methods().match( new Predicate<Method>(){

			@Override
			public Boolean classify(Method obj) {
				return obj.getName().equalsIgnoreCase("addPropertyChangeListener");
			}
			
		}).retriveAll();
		
		if (list.isEmpty()){
			throw new ReflectionException(source + " is not a bean");
		}
		try {
			list.getFirst().invoke(source, new Object[]{this});
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
		PropertyAccessor p = Introspector.of(target.getClass()).inspect().properties().named(event.getPropertyName()).retrive();
			
		p.setValue(target, event.getNewValue());
	}
}
