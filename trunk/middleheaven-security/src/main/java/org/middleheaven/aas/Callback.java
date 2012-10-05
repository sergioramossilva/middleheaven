package org.middleheaven.aas;

import java.io.Serializable;

/**
 * The credencial callabck. 
 * Callbacks allow to inquire the subject about needed credentials.
 */
public interface Callback extends Serializable {

	/**
	 * 
	 * @return <code>true</code> if the callback is not filled with information, <code>false</code>
	 * otherwise.
	 */
	boolean isBlank();

}
