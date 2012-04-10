/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
class PublishPointResolver implements Resolver {

	
	private Class<?> publishingType;
	private PublishPoint publishPoint;
	
	public PublishPointResolver (PublishPoint publishPoint , Class<?> publishingType){
		this.publishPoint = publishPoint;
		this.publishingType = publishingType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {
		
		Object factory = context.getInstanceFactory().getInstance(WiringQuery.search(publishingType));
		
		return this.publishPoint.getObject(context.getInstanceFactory(), factory);
		
	}

}
