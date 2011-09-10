/*
 * Created on 2006/10/15
 *
 */
package org.middleheaven.global.text;

import org.middleheaven.core.services.ServiceRegistry;


/**
 * Formats a reference <code>String</code> by translating it into a global displayable <code>String</code>
 * making use of the <code>LocalizationService</code>
 * 
 * 
 * @see org.middleheaven.global.text.LocalizationService
 */
public class GlobalFormatter implements Formatter<String> {

	public String format(String object) {
		return ServiceRegistry.getService(LocalizationService.class).getMessage( GlobalLabel.of(object) , false);

	}

	public String parse(String stringValue) {
		return stringValue;
	}

}
