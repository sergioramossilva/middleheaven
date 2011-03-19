package org.middleheaven.transactions;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.InterceptorProxyHandler;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.ConnectableBinder;
import org.middleheaven.core.wiring.InterceptionContext;
import org.middleheaven.core.wiring.InterceptorChain;
import org.middleheaven.core.wiring.WiringConnector;
import org.middleheaven.core.wiring.WiringInterceptor;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.EnhancedCollection;

public class AutoCommitTransactionServiceActivator extends Activator {
	
	private WiringService service;
	private AutoCommitTransactionService autoCommitTransactionService = new AutoCommitTransactionService();
	
	@Publish 
	public AutoCommitTransactionService getTestTransactionService(){
		return autoCommitTransactionService;
	}
	
	@Wire 
	public void setWiringService(WiringService service){
		this.service = service;
	}
	
	@Override
	public void activate(ActivationContext context) {
		
		service.addConnector(new WiringConnector(){

			@Override
			public void connect(ConnectableBinder binder) {
				binder.addInterceptor(new TransactionInterceptor());
			}

		});
	}

	@Override
	public void inactivate(ActivationContext context) {}

	
	
	private class TransactionInterceptor implements WiringInterceptor {

		@Override
		public <T> void intercept(InterceptionContext<T> context,	InterceptorChain<T> chain) {
			 chain.doChain(context);
			 
			 T object = context.getObject();
			 
			 object = (T)proxyfy(object, context.getTarget());
			 
			 context.setObject(object);
		}
		
		public Object proxyfy (final Object original, Class<?> type){
			
			EnhancedCollection<Method> all = Introspector.of(original.getClass()).inspect()
			.methods()
			.notInheritFromObject()
			.annotatedWith(Transactional.class).retriveAll();
			
			if (all.isEmpty()){
				return original;
			} else {
				
				final EnhancedCollection<String> names = all.collect(new Classifier<String,Method>(){

					@Override
					public String classify(Method obj) {
						return obj.getName();
					}
					
				});
				
				return Introspector.of(type).newProxyInstance(new InterceptorProxyHandler (original){

					@Override
					protected boolean willIntercept(Object proxy,
							Object[] args, MethodDelegator delegator)
							throws Throwable {
						return names.contains(delegator.getName());
					}
					
					@Override
					protected Object doInstead(Object proxy, Object[] args, MethodDelegator delegator) throws Throwable {
						Transaction t = autoCommitTransactionService.getTransaction();
						try {
							t.begin();
							Object o =  this.doOriginal(args, delegator);
							t.commit();
							return o;
						}catch (Throwable e){
							t.roolback();
							throw e;
						}
						
					}
					
				});
			}
		}
		
	}
}
