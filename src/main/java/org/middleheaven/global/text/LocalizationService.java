/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.global.text;

import org.middleheaven.global.Culture;
import org.middleheaven.global.CultureModel;




public interface LocalizationService  {

    /**
     * Return the correct locale based on the <code>System</code> properties.
     * @return
     */
    public Culture getExecutionEnvironmentCulture();
    
    /**
     * Returns the localeModel used by a specific application
     * @param applicationID
     * @return
     */
    public CultureModel getApplicationCultureModel(Object applicationID);
    
    /**
     * Register the localeModel used by a specific application
     * @param applicationID
     * @return
     */
    public void registerApplicationLocaleModel(Object applicationID ,CultureModel model);
    
    /**
     * Returns the localized text for a given localResource. If parameter <code>asMnemonic</code> is <code>false</code>
     * the mnemonic indicators are removed prior of returning the result. 
     * @param locale
     * @param localresource
     * @param asmnemonic
     * @return
     */
    public String getMessage(Culture culture , GlobalLabel localResource , boolean asMnemonic);
    
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
     * Returns a <code>TimestampFormatter</code> for the given <code>Locale</code>
     * 
     * @param locale
     * @return
     */
    public  TimestampFormatter getTimestampFormatter (Culture culture);
}
