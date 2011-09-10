package org.middleheaven.process;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.middleheaven.util.collections.AbstractEnumerableAdapter;
import org.middleheaven.util.collections.IteratorAdapter;



public abstract class AbstractAttributeContext implements AttributeContext {

	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		ContextScope[] scopes = ContextScope.values(); 
		
		for (ContextScope scope : scopes){
			T obj = this.getAttribute(scope, name, type);
			if (obj!=null){
				return obj;
			}
		}
		return null;
	}
	
	
	

}
