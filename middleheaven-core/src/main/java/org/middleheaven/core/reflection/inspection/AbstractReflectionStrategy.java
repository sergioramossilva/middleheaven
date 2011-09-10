package org.middleheaven.core.reflection.inspection;

import java.lang.reflect.Method;

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
			return CollectionUtils.emptySet();
		}
		
		type = getRealType(type);
		
		EnhancedArrayList<PropertyAccessor> result = new EnhancedArrayList<PropertyAccessor> ();
		for (Method m : Reflector.getReflector().getMethods(type)){
			
			if (!m.getName().startsWith("getClass") && m.getParameterTypes().length==0){
				if (m.getName().startsWith("get") ){
					result.add(getPropertyAccessor(type, StringUtils.firstLetterToLower(m.getName().substring(3))));
				} else if (m.getName().startsWith("is")){
					result.add(getPropertyAccessor(type, StringUtils.firstLetterToLower(m.getName().substring(2))));
				}

			}

		}
		return result;
	}


}
