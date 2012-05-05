/**
 * 
 */
package org.middleheaven.global;

import java.util.Collection;


/**
 * 
 */
public interface CultureModelFactory {

	public Collection<Culture> getSupportedCultures();
	   
	public CultureModel resolveModelFor(Culture culture);
}
