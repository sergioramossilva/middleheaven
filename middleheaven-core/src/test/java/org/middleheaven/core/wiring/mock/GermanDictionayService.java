/**
 * 
 */
package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.wiring.annotations.Component;


/**
 * 
 */
@GermanDicionary
@Component
public class GermanDictionayService extends HashDictionaryService {

	/**
	 * Constructor.
	 * @param lang
	 */
	public GermanDictionayService() {
		super("ge");
	}

}
