/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.middleheaven.core.Container;
import org.middleheaven.core.services.ContainerService;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.global.LocaleModel.Localizable;


/**
 * @author  Sergio M. M. Taborda 
 */
public class LocalizationServiceProvider extends ServiceActivator{

	RepositoryDomainBundle masterBundle;

	private Map<String, LocaleModel> models = new HashMap<String, LocaleModel>();
	LocalizationService service = new LocalizationServiceImpl();

	@Override
	public void activate(ServiceContext context) {
		Container environment = context.getService(ContainerService.class, null).getContainer();

		RepositoryDomainBundle masterBundle = new RepositoryDomainBundle();
		masterBundle.setRepository(environment.getEnvironmentConfigRepository());

		RepositoryDomainBundle childBundle = new RepositoryDomainBundle();
		childBundle.setRepository(environment.getAppConfigRepository());
		masterBundle.setChildBundle(childBundle);

	}


	@Override
	public void inactivate(ServiceContext context) {
		masterBundle = null;
		service = null;
	}

	private class LocalizationServiceImpl implements LocalizationService{

		public LocaleModel getApplicationLocaleModel(Object applicationID) {
			return models.get(applicationID.toString());
		}

		public void registerApplicationLocaleModel(Object applicationID, LocaleModel model) {
			models.put(applicationID.toString(), model);
		}

		public Locale getExecutionEnvironmentLocale() {
			return Locale.getDefault();
		}


		public String getMessage(GlobalLabel label, boolean asMnemonic) {
			return getMessage(getApplicationLocaleModel(null/*applicationID*/).getLocale(Localizable.TEXT), label,asMnemonic);
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
