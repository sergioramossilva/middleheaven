package org.middleheaven.core.wiring;



/**
 * Retrieves the object by dynamic constructions and automatic dependency injection 
 *
 * @param <T>
 */
public final class FactoryResolver implements Resolver {


	private BeanDependencyModel model;

	public static  FactoryResolver instanceFor(BeanDependencyModel model){
		return new FactoryResolver(model);
	}

	private FactoryResolver(BeanDependencyModel model){
		this.model = model;
	}

	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {
		// Determine witch constructor to invoke

		final ProducingWiringPoint producingPoint = model.getProducingWiringPoint();

		if (producingPoint == null){
			throw new ConfigurationException("Type " + model.getBeanClass() + " does not have a valid constructor");
		}

		Object instance = producingPoint.produceWith(context.getInstanceFactory());

		// fill requirements

		for (AfterWiringPoint a : model.getAfterPoints()){
			a.writeAtPoint(context.getInstanceFactory(), instance);
		}

		// initialize
		model.getPostCreatePoint().call(instance);

//		// publish services 
//		for (PublishPoint pp : model.getPublishPoints()){
//			Object object = pp.getObject(context.getInstanceFactory(), instance);
//
//			if (object !=null){
//				BeanDependencyModel objectBeanModel = binder.getBeanModel(object);
//				// add parameters in the publishing point to the characteristics of the bean
//				objectBeanModel.addParams(pp.getParams());
//
//				binder.addBindings(objectBeanModel, new InstanceResolver(object));
//
//
//			} else {
//				throw new IllegalStateException("");
//			}
//
//		}


		return  query.getContract().cast(instance);


	}


}
