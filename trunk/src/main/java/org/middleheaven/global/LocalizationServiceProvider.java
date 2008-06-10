/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.middleheaven.config.ConfigurationContext;
import org.middleheaven.config.ServiceConfigurationException;
import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.global.LocaleModel.Localizable;
import org.middleheaven.service.OneInstanceServiceProvider;
import org.middleheaven.service.ServiceException;

/**
 * @author  Sergio M. M. Taborda 
 */
public class LocalizationServiceProvider extends OneInstanceServiceProvider<LocalizationService> {

    RepositoryDomainBundle masterBundle;

    private Map<String, LocaleModel> models = new HashMap<String, LocaleModel>();
    

    public void config(ConfigurationContext context) throws ServiceConfigurationException {
        // TODO configurat politica de procura quando ha falha em encontrar o texto
        Container environment = context.getExecutionEnvironment();
        
        RepositoryDomainBundle masterBundle = new RepositoryDomainBundle();
        masterBundle.setRepository(environment.getEnvironmentConfigRepository());
        
        RepositoryDomainBundle childBundle = new RepositoryDomainBundle();
        childBundle.setRepository(environment.getAppConfigRepository());
        masterBundle.setChildBundle(childBundle);
        
        
    }
    

    public void stop() throws ServiceException {
        masterBundle = null;
        service = null;
    }
    
    public LocalizationService provide(Class serviceclass, Map params) throws ServiceException {
        return service;
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
            return getMessage(getApplicationLocaleModel(identifier).getLocale(Localizable.TEXT), label,asMnemonic);
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

	@Override
	protected LocalizationService instanciateService(
			org.middleheaven.core.Container environment) {
		return new LocalizationServiceImpl();
	}


}
