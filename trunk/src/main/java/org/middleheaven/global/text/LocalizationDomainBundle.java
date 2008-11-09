/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global.text;

import java.util.Locale;
import java.util.MissingResourceException;

/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class LocalizationDomainBundle {

    private LocalizationDomainBundle child= null;
    public  String localizeLabel(GlobalLabel label, Locale locale) throws MissingResourceException{
        
        String message =null;
        if (child!=null){
            try {
                message = child.localizeLabel(label, locale);
            }catch (MissingResourceException e){
                message = findLabel(label, locale);
            }
        }else{
            message = findLabel(label, locale);
        }
        return message;
    }
    
    public void setChildBundle(LocalizationDomainBundle child){
        this.child = child;
    }
    
    protected abstract  String findLabel(GlobalLabel label, Locale locale) throws MissingResourceException;
}
