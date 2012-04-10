/**
 * 
 */
package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.wiring.annotations.Component;
import org.middleheaven.core.wiring.annotations.Profile;

/**
 * 
 */
@Profile("production")
@Component
public class GreekDictionayService extends HashDictionaryService {

	/**
	 * Constructor.
	 * @param lang
	 */
	public GreekDictionayService() {
		super("gr");
	}

}
