/*
 * Created on 16/08/2005
 */
package org.middleheaven.logging;

import java.util.Map;

import org.middleheaven.logging.writters.StreamLogBookWriter;

/**
 *  A writer thar 
 */
class ConsoleLogWriter extends StreamLogBookWriter {

	/**
	 * 
	 * Constructor.
	 */
    ConsoleLogWriter(){
        this.out = System.out;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void config(Map params, LoggingConfiguration configuration) {}

}
