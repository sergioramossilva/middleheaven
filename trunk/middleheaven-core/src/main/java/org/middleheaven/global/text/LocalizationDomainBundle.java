/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global.text;

import java.util.MissingResourceException;

import org.middleheaven.global.Culture;

/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class LocalizationDomainBundle {

    private LocalizationDomainBundle child= null;
    
    public  String localizeLabel(TextLocalizable label, Culture culture) throws MissingResourceException{
        
        String message =null;
        if (child!=null){
            try {
                message = child.localizeLabel(label, culture);
            }catch (MissingResourceException e){
                message = findLabel(label, culture);
            }
        }else{
            message = findLabel(label, culture);
        }
        return message;
    }
    
    public void setChildBundle(LocalizationDomainBundle child){
        this.child = child;
    }
    
    protected abstract  String findLabel(TextLocalizable label, Culture culture) throws MissingResourceException;
}
