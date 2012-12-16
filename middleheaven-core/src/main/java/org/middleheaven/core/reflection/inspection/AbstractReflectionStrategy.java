package org.middleheaven.core.reflection.inspection;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.ReflectionPropertyAccessor;
import org.middleheaven.core.reflection.ReflectionStrategy;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedArrayList;
import org.middleheaven.util.collections.Enumerable;

public abstract class AbstractReflectionStrategy implements ReflectionStrategy{

	public PropertyAccessor getPropertyAccessor(Class<?> type, String propertyName){
		return new ReflectionPropertyAccessor(this.getRealType(type),propertyName);
	}
	
	@Override
	public Enumerable<PropertyAccessor> getPropertyAccessors(Class<?> type) throws ReflectionException {
		if (type==null){
			return CollectionUtils.emptyEnumerable();
		}
		
		type = getRealType(type);
		
		
		Set<String> propertyNames = new HashSet<String>();
		EnhancedArrayList<PropertyAccessor> result = new EnhancedArrayList<PropertyAccessor> ();
		
		for (Method m : Reflector.getReflector().getMethods(type)){
			
			if (m.getParameterTypes().length==0){
				if (!m.getName().startsWith("getClass")){
					if (m.getName().startsWith("get") ){
						final String propertyName = StringUtils.firstLetterToLower(m.getName().substring(3));
	
						if (propertyNames.add(propertyName)){
							result.add(getPropertyAccessor(type, propertyName));
						}
					
					} else if (m.getName().startsWith("is")){
						final String propertyName = StringUtils.firstLetterToLower(m.getName().substring(2));
						
						if (propertyNames.add(propertyName)){
							result.add(getPropertyAccessor(type, propertyName));
						}
					} 
				}
			} else if (m.getParameterTypes().length==1){
				if (m.getName().startsWith("set") ){
					final String propertyName = StringUtils.firstLetterToLower(m.getName().substring(3));

					if (propertyNames.add(propertyName)){
						result.add(getPropertyAccessor(type, propertyName));
					}
				
				} 
			}

		}
		return result;
	}


}
