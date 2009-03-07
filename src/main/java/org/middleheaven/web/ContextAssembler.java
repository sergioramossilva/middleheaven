package org.middleheaven.web;

import java.util.Collection;

import org.middleheaven.core.reflection.BeanAssembler;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.ui.Context;
import org.middleheaven.ui.ContextScope;

public class ContextAssembler implements BeanAssembler {

	Context context;
	ContextScope scope;
	
	public ContextAssembler(Context context, ContextScope scope) {
		this.context = context;
		this.scope = scope;
	}

	@Override
	public <B> B assemble(Class<B> type) {
		
		B instance = ReflectionUtils.newInstance(type);
		
		for (PropertyAccessor acessor:ReflectionUtils.getPropertyAccessors(type)){
			acessor.setValue(instance,context.getAttribute(scope,acessor.getName().toLowerCase(), acessor.getValueType()));
		}

		return instance;
	}

}
