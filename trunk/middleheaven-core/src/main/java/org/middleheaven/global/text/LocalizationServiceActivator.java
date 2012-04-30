/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global.text;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.middleheaven.core.bootstrap.FileContext;
import org.middleheaven.core.bootstrap.FileContextService;
import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.annotations.Component;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.global.Culture;
import org.middleheaven.global.CultureModel;


/**
 *
 */
@Component
public class LocalizationServiceActivator extends ServiceActivator{

	RepositoryDomainBundle masterBundle;

	private Map<String, CultureModel> models = new HashMap<String, CultureModel>();


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		//no-dependencies
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(LocalizationService.class));
	}
	
	
	@Override
	public void activate(ServiceContext serviceContext) {
		
	
		FileContext context = serviceContext.getService(FileContextService.class).getFileContext();

		masterBundle = new RepositoryDomainBundle();
		masterBundle.setRepository(context.getEnvironmentConfigRepository());

		RepositoryDomainBundle childBundle = new RepositoryDomainBundle();
		childBundle.setRepository(context.getAppConfigRepository());
		masterBundle.setChildBundle(childBundle);
		
		serviceContext.register(LocalizationService.class, new LocalizationServiceImpl());
	}


	@Override
	public void inactivate(ServiceContext serviceContext) {
		masterBundle = null;
	}

	@Service
	private class LocalizationServiceImpl implements LocalizationService{

		public CultureModel getApplicationCultureModel(Object applicationID) {
			return models.get(applicationID.toString());
		}

		public void registerApplicationLocaleModel(Object applicationID, CultureModel model) {
			models.put(applicationID.toString(), model);
		}

		public Locale getExecutionEnvironmentLocale() {
			return Locale.getDefault();
		}


		public String getMessage(GlobalLabel label, boolean asMnemonic) {
			return getMessage(this.getExecutionEnvironmentCulture(), label, asMnemonic); 
		}

		@Override
		public String getMessage(Culture culture, GlobalLabel localResource, boolean asMnemonic) {
			String message = masterBundle.localizeLabel(localResource, culture.toLocale());
			if (asMnemonic){
				return message.replaceAll("&", "");
			}
			return message;
		}

		@Override
		public Culture getExecutionEnvironmentCulture() {
			return Culture.defaultValue();
		}

		@Override
		public QuantityFormatter getQuantityFormatter(Culture culture) {
			return new QuantityFormatter(culture);
		}

		@Override
		public TimepointFormatter getTimestampFormatter(Culture culture) {
			return new TimepointFormatter(culture);
		}

		



	}






}
