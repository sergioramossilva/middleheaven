package org.middleheaven.core.wiring;


/**
 * Retrieves the object by dynamic constructions and automatic dependency injection 
 *
 * @param <T>
 */
public final class FactoryResolver<T> implements Resolver<T> {

	private EditableBinder binder;
	private Class<T> type;

	@SuppressWarnings("unchecked")
	public static <I> FactoryResolver<I> instanceFor(Class<?> type , EditableBinder binder){
		return new FactoryResolver(type,binder);
	}
	
	private FactoryResolver(Class<T> type , EditableBinder binder){
		this.binder = binder;
		this.type = type;
	}
	
	@Override
	public T resolve(WiringSpecification<T> query) {
		// Determine witch constructor to invoke

		WiringModel model = binder.getWiringModel(type);
		
		T instance = model.getConstructorPoint().construct(binder);
		
		// TODO intercep // call PostCreate

		return query.getContract().cast(instance);
 

	}


}
