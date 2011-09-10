/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.global;

import java.text.Format;

import org.middleheaven.global.text.writeout.NumberWriteoutFormat;

/**
 * Represents an application specific Culture selection factory.
 * Localizable resources can belong in different locales. In particular they can be 
 * submited to a unique locale. That choice is application specific
 *  
 *
 */
public interface CultureModel {


    public Format getDateFormat();
    public Format getNumberFormat();
    public NumberWriteoutFormat getNumberWriteoutFormat();
    
}
