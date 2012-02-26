package org.middleheaven.core.wiring;

import org.middleheaven.core.wiring.activation.PublishPoint;


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

		BeanModel model = binder.getBeanModel(type);

		final ProducingWiringPoint producingPoint = model.getProducingWiringPoint();

		if (producingPoint == null){
			throw new ConfigurationException("Type " + type + " does not have a valid constructor");
		}

		Object instance = producingPoint.produceWith(binder);

		// fill requirements

		for (AfterWiringPoint a : model.getAfterPoints()){
			a.writeAtPoint(binder, instance);
		}

		// initialize
		model.getPostCreatePoint().call(instance);

		// publish services 
		for (PublishPoint pp : model.getPublishPoints()){
			Object object = pp.getObject(instance);

			if (object !=null){
				BeanModel objectBeanModel = binder.getBeanModel(object);
				// add parameters in the publishing point to the characteristics of the bean
				objectBeanModel.addParams(pp.getParams());

				binder.addBindings(objectBeanModel, new InstanceResolver<Object>(object));


			} else {
				throw new IllegalStateException("");
			}

		}


		return query.getContract().cast(instance);


	}


}
