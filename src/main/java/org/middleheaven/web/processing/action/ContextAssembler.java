package org.middleheaven.web.processing.action;

import org.middleheaven.core.reflection.BeanAssembler;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.wiring.ObjectPool;
import org.middleheaven.ui.AttributeContext;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.util.collections.Walker;

public class ContextAssembler implements BeanAssembler {

	AttributeContext context;
	ContextScope scope;
	private ObjectPool pool;
	
	public ContextAssembler(ObjectPool pool,AttributeContext context, ContextScope scope) {
		this.context = context;
		this.scope = scope;
		this.pool = pool;
	}

	@Override
	public <B> B assemble(Class<B> type) {
		
		final B instance = pool.getInstance(type);
		
		Introspector.of(type).properties().each(new Walker<PropertyAccessor>(){

			@Override
			public void doWith(PropertyAccessor acessor) {
				acessor.setValue(instance,context.getAttribute(scope,acessor.getName().toLowerCase(), acessor.getValueType()));
			}
			
		});

		return instance;
	}

}
