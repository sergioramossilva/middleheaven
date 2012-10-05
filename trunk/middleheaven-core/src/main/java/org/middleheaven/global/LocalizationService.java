/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.global;

import java.util.Collection;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.global.text.LocalizationDomainBundle;
import org.middleheaven.global.text.QuantityFormatter;
import org.middleheaven.global.text.TimepointFormatter;


/**
 * Localization Service.
 * 
 */
@Service
public interface LocalizationService  {

    /**
     * Return the environement default {@link Culture}.
     * @return the environement default {@link Culture}.
     */
    public Culture getExecutionEnvironmentCulture();
    
    /**
     * the supported collection of cultures.
     * @return the supported collection of cultures.
     */
    public Collection<Culture> getSupportedCultures();
    
    /**
     * Returns the {@link CultureModel} correspondent with a given {@link Culture}.
     * 
     * @param culture the given {@link Culture}.
     * @return the corrsponding {@link CultureModel}.
     */
    public CultureModel getCultureModel(Culture culture);
    
    /**
     * Register the localeModel used by a specific application
     * 
     * @param cultureModelFactory the {@link CultureModelFactory} to register.
     * 
     */
    public void registerCultureModelFactory(CultureModelFactory cultureModelFactory);
    
    /**
     * Returns the localized text for a given localResource. If parameter <code>asMnemonic</code> is <code>false</code>
     * the mnemonic indicators are removed prior of returning the result. 
     * @param label the resource label
     * @param culture the {@link Culture} to use in the label localization.
     *
     * @return the localizaed message for the label in the given {@link Culture}.
     */
    public String getMessage(TextLocalizable label , Culture culture);
    

    /**
     * 
     * @param bundle
     */
    public void registerLocalizationDomainBundle(LocalizationDomainBundle bundle);
    
    

}
