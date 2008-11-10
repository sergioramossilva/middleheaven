/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global.text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.Require;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.global.CultureModel;


/**
 * @author  Sergio M. M. Taborda 
 */
public class LocalizationServiceProvider extends ServiceActivator{

	RepositoryDomainBundle masterBundle;

	private Map<String, CultureModel> models = new HashMap<String, CultureModel>();
	LocalizationService service = new LocalizationServiceImpl();
	private BootstrapService bootstrapService;
	
	@Require
	public void setBootstrapService(BootstrapService bootstrapService) {
		this.bootstrapService = bootstrapService;
	}

	@Publish
	public LocalizationService getLocalizationService(){
		return service;
	}
	
	@Override
	public void activate(ServiceAtivatorContext context) {
		Container environment = bootstrapService.getContainer();

		RepositoryDomainBundle masterBundle = new RepositoryDomainBundle();
		masterBundle.setRepository(environment.getEnvironmentConfigRepository());

		RepositoryDomainBundle childBundle = new RepositoryDomainBundle();
		childBundle.setRepository(environment.getAppConfigRepository());
		masterBundle.setChildBundle(childBundle);

	}


	@Override
	public void inactivate(ServiceAtivatorContext context) {
		masterBundle = null;
		service = null;
	}

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
			return null; // TODO
		}

		public String getMessage(Locale locale, GlobalLabel label, boolean asMnemonic) {
			String message = masterBundle.localizeLabel(label, locale);
			if (asMnemonic){
				return message.replaceAll("&", "");
			}
			return message;
		}



		public void dispose() {}

		@Override
		public <T> Formatter<T> getFormatter(Class<T> type, Locale locale) {
			// TODO Auto-generated method stub
			return null;
		}



	}






}
