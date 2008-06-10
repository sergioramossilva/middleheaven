/*
 * Created on 2006/10/15
 *
 */
package org.middleheaven.global;

import org.middleheaven.service.ServiceRegistry;

/**
 * Formats a reference <code>String</code> by translating it into a global displayable <code>String</code>
 * making use of the <code>LocalizationService</code>
 * 
 * @author Sergio M. M. Taborda 
 * @see org.middleheaven.global.LocalizationService
 */
public class GlobalFormatter implements Formatter<String> {

	public String format(String object) {
		return ServiceRegistry.getService(LocalizationService.class).getMessage( GlobalLabel.newInstance(object) , false);

	}

	public String parse(String stringValue) {
		return stringValue;
	}

}
