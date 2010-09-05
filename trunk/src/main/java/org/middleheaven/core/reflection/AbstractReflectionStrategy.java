package org.middleheaven.core.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.middleheaven.util.StringUtils;

public abstract class AbstractReflectionStrategy implements ReflectionStrategy{

	public PropertyAccessor getPropertyAccessor(Class<?> type, String propertyName){
		return new ReflectionPropertyAccessor(this.getRealType(type),propertyName);
	}
	
	@Override
	public Iterable<PropertyAccessor> getPropertyAccessors(Class<?> type) throws ReflectionException {
		if (type==null){
			return Collections.emptySet();
		}
		
		type = getRealType(type);
		
		Collection<PropertyAccessor> result = new ArrayList<PropertyAccessor> ();
		for (Method m : ReflectionUtils.getMethods(type)){
			
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
