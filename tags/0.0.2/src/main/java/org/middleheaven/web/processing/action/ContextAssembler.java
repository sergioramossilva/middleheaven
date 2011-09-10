package org.middleheaven.web.processing.action;

import org.middleheaven.core.reflection.BeanAssembler;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.ObjectPool;
import org.middleheaven.ui.AttributeContext;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.util.collections.Walker;

public class ContextAssembler implements BeanAssembler {

	AttributeContext context;
	ContextScope scope;
	private ObjectPool pool;
	private String objectName;
	
	public ContextAssembler(ObjectPool pool,AttributeContext context, ContextScope scope, String objectName) {
		this.context = context;
		this.scope = scope;
		this.pool = pool;
		this.objectName = objectName;
	}

	@Override
	public <B> B assemble(final Class<B> type) {
		
		final B instance = pool.getInstance(type);
		
		Introspector.of(type).inspect().properties().each(new Walker<PropertyAccessor>(){

			@Override
			public void doWith(PropertyAccessor acessor) {
				acessor.setValue(instance,context.getAttribute(
						scope,
						(objectName ==null ? "" : objectName + ".") + acessor.getName(), 
						acessor.getValueType()
						)
				);
			}
			
		});

		return instance;
	}

}
