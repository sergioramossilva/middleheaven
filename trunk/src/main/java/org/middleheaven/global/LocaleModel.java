/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.global;

import java.util.Locale;

/**
 * Represents a application specific locale factory.
 * Localizable resources can belong in different locales. In particular they can be 
 * submited to a unique locale. That choice is application specific
 *  
 * @author Sergio M. M. Taborda 
 *
 */
public interface LocaleModel {

    /**
     * @author  Sergio M. M. Taborda 
     */
    public enum Localizable {
        TEXT,NUMBERS_FORMAT,DATES_FORMAT,CALENDAR_MODEL
    }
    
    public Locale getLocale(Localizable type);
}
