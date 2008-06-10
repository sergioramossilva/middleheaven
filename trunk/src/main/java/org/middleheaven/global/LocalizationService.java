/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.global;

import java.util.Locale;


import org.middleheaven.service.Service;

@Service
public interface LocalizationService  {

    /**
     * Return the correct locale based on the <code>System</code> properties.
     * @return
     */
    public Locale getExecutionEnvironmentLocale();
    
    /**
     * Returns the localeModel used by a specific application
     * @param applicationID
     * @return
     */
    public LocaleModel getApplicationLocaleModel(Object applicationID);
    
    /**
     * Register the localeModel used by a specific application
     * @param applicationID
     * @return
     */
    public void registerApplicationLocaleModel(Object applicationID ,LocaleModel model);
    
    /**
     * Returns the localized text for a given localResource. If parameter <code>asMnemonic</code> is <code>false</code>
     * the mnemonic indicators are removed prior of returning the result. 
     * @param locale
     * @param localresource
     * @param asmnemonic
     * @return
     */
    public String getMessage(Locale locale , GlobalLabel localResource , boolean asMnemonic);
    
    /**
     * Returns the localized text for a given localResource. If parameter <code>asMnemonic</code> is <code>false</code>
     * the mnemonic indicators are removed prior of returning the result. The locale used for translation
     * is gathered from   getApplicationLocaleModel
     * @param localResource
     * @param asMnemonic
     * @return
     */
    public String getMessage(GlobalLabel localResource , boolean asMnemonic);
    
    
    /**
     * Returns a <code>Formatter</code> for the given <code>Locale</code> and object <code>Class</code>
     * @param type
     * @param locale
     * @return
     */
    public <T> Formatter<T> getFormatter (Class<T> type, Locale locale);
}
