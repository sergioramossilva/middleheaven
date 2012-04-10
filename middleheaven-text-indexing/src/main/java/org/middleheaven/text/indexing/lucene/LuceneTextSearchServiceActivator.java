/**
 * 
 */
package org.middleheaven.text.indexing.lucene;

import java.util.Collection;

import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.text.indexing.TextIndexingService;

/**
 * 
 */
public class LuceneTextSearchServiceActivator extends ServiceActivator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(
			Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(TextIndexingService.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		

		serviceContext.register(TextIndexingService.class, new LuceneTextIndexingService());
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

}
