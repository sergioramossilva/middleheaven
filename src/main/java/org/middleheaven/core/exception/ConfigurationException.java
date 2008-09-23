/*
 * Created on 5/Ago/2006
 *
 */
package org.middleheaven.core.exception;


public class ConfigurationException extends RuntimeException {

    public ConfigurationException(Exception e) {
        super(e);
    }

	public ConfigurationException(String message) {
		 super(message);
	}



}
